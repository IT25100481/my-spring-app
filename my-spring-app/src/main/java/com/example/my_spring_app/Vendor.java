package com.example.my_spring_app;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

// Removed JPA annotations for text file persistence
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    private Long id;

    private User user;

    private String businessName;

    private String category;

    private String businessDescription;

    private String businessPhone;

    private String website;

    private String businessLocation;

    private String serviceAreas;

    private String businessImageUrl;

    private String bannerImageUrl;

    private String profilePhotoUrl;

    private String portfolioMediaUrls;

    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private Boolean isActive = true;

    private Double averageRating = 0.0;

    private Integer totalReviews = 0;

    private Integer totalBookings = 0;

    private Double monthlyEarnings = 0.0;

    private Integer profileViews = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;

    private List<String> services;

    // Removed JPA relationships for text file persistence

    public Vendor(String businessName, String email, String password, String phone, String category) {
        this.businessName = businessName;
        this.businessPhone = phone;
        this.category = category;
        this.verificationStatus = VerificationStatus.PENDING;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        
        this.user = new User();
        this.user.setFullName(businessName);
        this.user.setEmail(email);
        this.user.setPassword(password);
        this.user.setPhone(phone);
        this.user.setRole(UserRole.VENDOR);
    }
}
