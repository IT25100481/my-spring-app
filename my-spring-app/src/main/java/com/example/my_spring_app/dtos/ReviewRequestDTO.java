package com.example.my_spring_app.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request DTO for submitting/updating reviews
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @NotBlank(message = "Review text is required")
    @Size(min = 10, max = 2000, message = "Review must be between 10 and 2000 characters")
    private String reviewText;
}
