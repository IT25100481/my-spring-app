package com.example.my_spring_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    // ── USER REGISTRATION ──
    @PostMapping("/register/user")
    public ResponseEntity<Map<String, String>> registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone) {

        try {
            // Check if email already exists
            if (userService.emailExists(email)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", "error",
                                "message", "Email already registered!"));
            }

            // Save user
            User user = new User(fullName, email, password, phone);
            userService.saveUser(user);

            return ResponseEntity.ok()
                    .body(Map.of("status", "success",
                            "message", "User registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("status", "error",
                            "message", "Something went wrong. Try again."));
        }
    }

    // ── VENDOR REGISTRATION ──
    @PostMapping("/register/vendor")
    public ResponseEntity<Map<String, String>> registerVendor(
            @RequestParam String businessName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String category) {

        try {
            // Check if email already exists
            if (vendorService.emailExists(email)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("status", "error",
                                "message", "Email already registered!"));
            }

            // Save vendor
            Vendor vendor = new Vendor(businessName, email, password, phone, category);
            vendorService.saveVendor(vendor);

            return ResponseEntity.ok()
                    .body(Map.of("status", "success",
                            "message", "Vendor registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("status", "error",
                            "message", "Something went wrong. Try again."));
        }
    }

    // ── GET CATEGORIES ──
    @GetMapping("/categories.txt")
    public ResponseEntity<String> getCategories() {
        try {
            Path path = Paths.get("categories.txt");
                if (!Files.exists(path)) {
                return ResponseEntity.ok()
                    .header("Content-Type", "text/plain")
                    .body("");
                }
                String content = Files.readString(path);
                return ResponseEntity.ok()
                    .header("Content-Type", "text/plain")
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .header("Content-Type", "text/plain")
                    .body("");
        }
    }
}