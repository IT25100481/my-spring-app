package com.example.my_spring_app.services;

import com.example.my_spring_app.models.Review;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReviewManagementService {

    public List<Review> getVendorReviews(String email) {
        return Collections.emptyList();
    }

    public Review respondToReview(String email, Long reviewId, String response) {
        throw new UnsupportedOperationException("Review management is not available with text-file persistence yet.");
    }

    public Review flagReview(String email, Long reviewId, String reason) {
        throw new UnsupportedOperationException("Review management is not available with text-file persistence yet.");
    }

    public Review createReview(String customerEmail, Long bookingId, Integer rating, String text) {
        throw new UnsupportedOperationException("Review management is not available with text-file persistence yet.");
    }
}
