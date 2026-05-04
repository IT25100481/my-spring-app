package com.example.my_spring_app.dtos;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Response DTO for review data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private Long id;
    private Long vendorId;
    private String vendorName;
    private Long customerId;
    private String customerName;
    private Integer rating;
    private String reviewText;
    private String vendorResponse;
    private LocalDateTime vendorResponseAt;
    private Boolean isApproved;
    private Boolean isFlagged;
    private String flagReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Explicit getter for Lombok compatibility
    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
