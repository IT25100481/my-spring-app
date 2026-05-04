package com.example.my_spring_app.services;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.Review;
import com.example.my_spring_app.repositories.BookingRepository;
import com.example.my_spring_app.repositories.ReviewRepository;
import com.example.my_spring_app.repositories.UserRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewManagementService {

    private final ReviewRepository reviewRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public ReviewManagementService(ReviewRepository reviewRepository,
                                   VendorRepository vendorRepository,
                                   UserRepository userRepository,
                                   BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Review> getVendorReviews(String email) {
        Vendor vendor = resolveVendor(email);
        return reviewRepository.findByVendor_Id(vendor.getId());
    }

    public Review respondToReview(String email, Long reviewId, String response) {
        Vendor vendor = resolveVendor(email);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this review");
        }
        review.setVendorResponse(response);
        review.setVendorResponseAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review flagReview(String email, Long reviewId, String reason) {
        Vendor vendor = resolveVendor(email);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        if (!review.getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this review");
        }
        review.setIsFlagged(true);
        review.setFlagReason(reason);
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review createReview(String customerEmail, Long bookingId, Integer rating, String text) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Review review = new Review();
        review.setCustomer(customer);
        review.setVendor(booking.getVendor());
        review.setBooking(booking);
        review.setRating(rating);
        review.setRatingScore(rating.floatValue());
        review.setReviewText(text);
        review.setIsApproved(true);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        updateVendorStats(saved.getVendor());
        return saved;
    }

    private void updateVendorStats(Vendor vendor) {
        List<Review> reviews = reviewRepository.findByVendor_Id(vendor.getId());
        if (reviews.isEmpty()) {
            vendor.setAverageRating(0.0);
            vendor.setTotalReviews(0);
        } else {
            double avg = reviews.stream().mapToDouble(review -> review.getRatingScore() == null ? 0.0 : review.getRatingScore()).average().orElse(0.0);
            vendor.setAverageRating(avg);
            vendor.setTotalReviews(reviews.size());
        }
        vendorRepository.save(vendor);
    }

    private Vendor resolveVendor(String email) {
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}
