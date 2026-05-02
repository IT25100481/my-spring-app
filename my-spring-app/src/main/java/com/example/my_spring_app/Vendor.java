package com.example.my_spring_app;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.example.my_spring_app.models.Availability;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.Review;
import com.example.my_spring_app.models.VendorServiceEntity;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String businessDescription;

    @Column(nullable = false)
    private String businessPhone;

    private String website;

    private String businessLocation;

    @Column(length = 500)
    private String serviceAreas;

    @Column(length = 500)
    private String businessImageUrl;

    @Column(length = 500)
    private String bannerImageUrl;

    @Column(length = 500)
    private String profilePhotoUrl;

    @Column(length = 2000)
    private String portfolioMediaUrls;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(nullable = false)
    private Boolean isActive = true;

    private Double averageRating = 0.0;

    private Integer totalReviews = 0;

    private Integer totalBookings = 0;

    private Double monthlyEarnings = 0.0;

    private Integer profileViews = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<VendorServiceEntity> services;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Availability> availabilities;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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
