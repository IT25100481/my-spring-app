package com.example.my_spring_app.controllers;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.dtos.*;
import com.example.my_spring_app.security.JwtTokenProvider;
import com.example.my_spring_app.services.AuthService;
import com.example.my_spring_app.services.EmailService;
import com.example.my_spring_app.UserService;
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
import java.util.Optional;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

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
            String token = jwtTokenProvider.generateTokenFromUser(user);

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
     * Forgot password - send reset email
     * POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new AuthResponse("error", "Email is required"));
            }

            // Check if user exists
            Optional<User> userOptional = userService.findByEmailAndIsActiveTrue(email);
            if (userOptional.isEmpty()) {
                // Don't reveal if email exists or not for security
                return ResponseEntity.ok(new AuthResponse("success", "If an account with that email exists, we've sent you a password reset link."));
            }

            User user = userOptional.get();

            // Send password reset email
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName());

            return ResponseEntity.ok(new AuthResponse("success", "If an account with that email exists, we've sent you a password reset link."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("error", "Failed to process request: " + e.getMessage()));
        }
    }
}