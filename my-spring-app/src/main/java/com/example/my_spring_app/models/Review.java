package com.example.my_spring_app.models;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private Float ratingScore;

    @Column(length = 2000)
    private String reviewText;

    @Column(length = 1000)
    private String vendorResponse;

    @Column(nullable = false)
    private Boolean isApproved = true;

    @Column(nullable = false)
    private Boolean isFlagged = false;

    @Column(length = 500)
    private String flagReason;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime vendorResponseAt;

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
