package com.example.my_spring_app.models;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Review Model - File-based persistence
 * Represents a customer review for a vendor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private Long id;
    private Vendor vendor;
    private User customer;
    private Integer rating;
    private Float ratingScore;
    private String reviewText;
    private String vendorResponse;
    private Boolean isApproved = true;
    private Boolean isFlagged = false;
    private String flagReason;
    private LocalDateTime createdAt;
    private LocalDateTime vendorResponseAt;
    private LocalDateTime updatedAt;
}
