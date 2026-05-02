package com.example.my_spring_app.controllers;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.dtos.*;
import com.example.my_spring_app.security.JwtTokenProvider;
import com.example.my_spring_app.services.AuthService;
import com.example.my_spring_app.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Register a new customer user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            // Validate input
            if (request.getFullName() == null || request.getFullName().trim().isEmpty() ||
                request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty() ||
                request.getPhone() == null || request.getPhone().trim().isEmpty()) {
                
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("error", "All fields are required"));
            }

            // Register user
            User user = authService.registerUser(
                    request.getFullName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse("success", "User registered successfully. Please check your email."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("error", "Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Register a new vendor
     * POST /api/auth/register-vendor
     */
    @PostMapping("/register-vendor")
    public ResponseEntity<?> registerVendor(@RequestBody RegisterVendorRequest request) {
        try {
            // Validate input
            if (request.getBusinessName() == null || request.getBusinessName().trim().isEmpty() ||
                request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty() ||
                request.getPhone() == null || request.getPhone().trim().isEmpty() ||
                request.getCategory() == null || request.getCategory().trim().isEmpty()) {
                
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("error", "All fields are required"));
            }

            // Register vendor
            Vendor vendor = authService.registerVendor(
                    request.getBusinessName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getPhone(),
                    request.getCategory()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse("success", 
                            "Vendor registered successfully. Your profile is pending admin approval."));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("error", "Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Login user or vendor
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("error", "Email and password are required"));
            }

            // Authenticate user
            User user = authService.authenticateUser(request.getEmail(), request.getPassword());

            // Generate JWT token
            String token = jwtTokenProvider.generateTokenFromEmail(user.getEmail());

            // Create response
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().toString()
            );

            return ResponseEntity.ok(new AuthResponse(
                    "success",
                    "Login successful",
                    token,
                    userInfo
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("error", "Login failed: " + e.getMessage()));
        }
    }

    /**
     * Change password
     * POST /api/auth/change-password
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
                                           @RequestBody Map<String, String> request) {
        try {
            // Extract user email from token
            String email = jwtTokenProvider.getUserEmailFromToken(token.replace("Bearer ", ""));
            
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("error", "Invalid token"));
            }

            User user = authService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");

            if (oldPassword == null || newPassword == null) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("error", "Old and new passwords are required"));
            }

            authService.changePassword(user.getId(), oldPassword, newPassword);

            return ResponseEntity.ok(new AuthResponse("success", "Password changed successfully"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("error", "Password change failed: " + e.getMessage()));
        }
    }

    /**
     * Request password reset
     * POST /api/auth/password-reset-request
     */
    @PostMapping("/password-reset-request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("error", "Email is required"));
            }

            if (!authService.emailExists(email)) {
                // For security, return success even if email doesn't exist
                return ResponseEntity.ok(new AuthResponse("success", 
                        "If an account with that email exists, a password reset link will be sent"));
            }

            // TODO: Generate reset token and send email
            // This requires additional implementation with token storage
            
            return ResponseEntity.ok(new AuthResponse("success", 
                    "If an account with that email exists, a password reset link will be sent"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("error", "Password reset request failed: " + e.getMessage()));
        }
    }

    /**
     * Verify token
     * POST /api/auth/verify-token
     */
    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            
            if (!jwtTokenProvider.validateToken(cleanToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("error", "Invalid token"));
            }

            String email = jwtTokenProvider.getUserEmailFromToken(cleanToken);
            User user = authService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("valid", true);
            response.put("email", email);
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("error", "Token verification failed"));
        }
    }

    /**
     * Refresh token
     * POST /api/auth/refresh-token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            
            if (!jwtTokenProvider.validateToken(cleanToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("error", "Invalid token"));
            }

            String email = jwtTokenProvider.getUserEmailFromToken(cleanToken);
            String newToken = jwtTokenProvider.generateTokenFromEmail(email);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("token", newToken);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("error", "Token refresh failed"));
        }
    }

    /**
     * Get current user info
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtTokenProvider.getUserEmailFromToken(token.replace("Bearer ", ""));
            User user = authService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().toString()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("user", userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("error", "Could not retrieve user info"));
        }
    }
}
