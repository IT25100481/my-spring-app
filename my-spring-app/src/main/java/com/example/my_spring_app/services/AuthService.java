package com.example.my_spring_app.services;

import com.example.my_spring_app.User;
import com.example.my_spring_app.UserRole;
import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.VerificationStatus;
import com.example.my_spring_app.repositories.UserRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    /**
     * Register a new customer user
     */
    public User registerUser(String fullName, String email, String password, String phone) {
        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(UserRole.CUSTOMER);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        
        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFullName());
        
        return savedUser;
    }

    /**
     * Register a new vendor
     */
    public Vendor registerVendor(String businessName, String email, String password, 
                                String phone, String category) {
        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create user account for vendor
        User user = new User();
        user.setFullName(businessName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(UserRole.VENDOR);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Create vendor profile
        Vendor vendor = new Vendor();
        vendor.setUser(savedUser);
        vendor.setBusinessName(businessName);
        vendor.setBusinessPhone(phone);
        vendor.setCategory(category);
        vendor.setIsActive(true);
        vendor.setVerificationStatus(VerificationStatus.PENDING);

        Vendor savedVendor = vendorRepository.save(vendor);

        // Send vendor registration email
        emailService.sendVendorRegistrationEmail(email, businessName);

        return savedVendor;
    }

    /**
     * Authenticate user and return user object if valid
     */
    public User authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmailAndIsActiveTrue(email);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOptional.get();
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Reset password with token
     */
    public void resetPassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        // Send password reset confirmation email
        emailService.sendPasswordResetEmail(email, user.getFullName());
    }

    /**
     * Change password (requires old password verification)
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }

    /**
     * Get vendor by user ID
     */
    public Optional<Vendor> getVendorByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return vendorRepository.findByUser_Email(user.get().getEmail());
        }
        return Optional.empty();
    }

    /**
     * Check if email exists
     */
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Approve vendor profile (admin function)
     */
    public void approveVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setVerificationStatus(VerificationStatus.APPROVED);
        vendor.setApprovedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendor);
        
        // Send approval email
        emailService.sendVendorApprovalEmail(vendor.getUser().getEmail(), vendor.getBusinessName());
    }

    /**
     * Reject vendor profile (admin function)
     */
    public void rejectVendor(Long vendorId, String reason) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setVerificationStatus(VerificationStatus.REJECTED);
        vendor.setUpdatedAt(LocalDateTime.now());

        vendorRepository.save(vendor);
        
        // Send rejection email
        emailService.sendVendorRejectionEmail(vendor.getUser().getEmail(), 
                vendor.getBusinessName(), reason);
    }
}
