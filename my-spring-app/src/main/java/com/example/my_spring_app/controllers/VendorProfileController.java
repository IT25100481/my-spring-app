package com.example.my_spring_app.controllers;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.dtos.VendorProfileResponse;
import com.example.my_spring_app.dtos.VendorProfileUpdateRequest;
import com.example.my_spring_app.security.JwtTokenProvider;
import com.example.my_spring_app.services.VendorProfileService;
import com.example.my_spring_app.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/profile")
@CrossOrigin(origins = "*")
public class VendorProfileController {

    private final VendorProfileService vendorProfileService;
    private final VendorService vendorService;
    private final JwtTokenProvider jwtTokenProvider;

    public VendorProfileController(VendorProfileService vendorProfileService,
                                   VendorService vendorService,
                                   JwtTokenProvider jwtTokenProvider) {
        this.vendorProfileService = vendorProfileService;
        this.vendorService = vendorService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<?> getMyProfile(@RequestHeader(value = "Authorization", required = false) String authorization,
                                          @RequestParam(value = "email", required = false) String email) {
        try {
            String resolvedEmail = resolveEmail(authorization, email);
            if (resolvedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(error("Authorization token or email is required"));
            }

            VendorProfileResponse response = vendorProfileService.getVendorProfileByEmail(resolvedEmail);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(error(ex.getMessage()));
        }
    }

    @GetMapping("/{vendorId}")
    public ResponseEntity<?> getVendorProfile(@PathVariable Long vendorId) {
        try {
            return ResponseEntity.ok(vendorProfileService.getPublicVendorProfile(vendorId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(error(ex.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<VendorProfileResponse>> searchVendors(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(vendorProfileService.searchVendors(category, location));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestHeader(value = "Authorization", required = false) String authorization,
                                           @RequestParam(value = "email", required = false) String email,
                                           @RequestBody VendorProfileUpdateRequest request) {
        try {
            String resolvedEmail = resolveEmail(authorization, email);
            if (resolvedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(error("Authorization token or email is required"));
            }

            Vendor vendor = vendorService.findByUserEmail(resolvedEmail)
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));

            return ResponseEntity.ok(vendorProfileService.updateVendorProfile(vendor.getId(), request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(ex.getMessage()));
        }
    }

    @PostMapping("/photo")
    public ResponseEntity<?> uploadProfilePhoto(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                @RequestParam(value = "email", required = false) String email,
                                                @RequestParam("file") MultipartFile file) {
        try {
            String resolvedEmail = resolveEmail(authorization, email);
            if (resolvedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(error("Authorization token or email is required"));
            }

            Vendor vendor = vendorService.findByUserEmail(resolvedEmail)
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));

            return ResponseEntity.ok(vendorProfileService.uploadProfilePhoto(vendor.getId(), file));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(ex.getMessage()));
        }
    }

    @PostMapping("/portfolio")
    public ResponseEntity<?> uploadPortfolioMedia(@RequestHeader(value = "Authorization", required = false) String authorization,
                                                  @RequestParam(value = "email", required = false) String email,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            String resolvedEmail = resolveEmail(authorization, email);
            if (resolvedEmail == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(error("Authorization token or email is required"));
            }

            Vendor vendor = vendorService.findByUserEmail(resolvedEmail)
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));

            return ResponseEntity.ok(vendorProfileService.uploadPortfolioMedia(vendor.getId(), file));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(error(ex.getMessage()));
        }
    }

    private String resolveEmail(String authorization, String email) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String resolved = jwtTokenProvider.getUserEmailFromToken(token);
            if (resolved != null) {
                return resolved;
            }
        }
        return email;
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
