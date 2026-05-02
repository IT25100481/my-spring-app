package com.example.my_spring_app.services;

import com.example.my_spring_app.User;
import com.example.my_spring_app.UserService;
import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.VendorService;
import com.example.my_spring_app.dtos.ReviewRequestDTO;
import com.example.my_spring_app.dtos.ReviewResponseDTO;
import com.example.my_spring_app.models.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Review Management Service - File-based persistence
 * Manages customer reviews for vendors with vendor responses and admin controls
 */
@Service
public class ReviewService {

    private static final String FILE_PATH = "reviews.txt";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    private final UserService userService;
    private final VendorService vendorService;

    public ReviewService(UserService userService, VendorService vendorService) {
        this.userService = userService;
        this.vendorService = vendorService;
    }

    /**
     * Submit a new review
     */
    public synchronized ReviewResponseDTO submitReview(Long customerId, ReviewRequestDTO request) {
        // Validate inputs
        Optional<User> customer = userService.findById(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<Vendor> vendor = vendorService.findById(request.getVendorId());
        if (vendor.isEmpty()) {
            throw new RuntimeException("Vendor not found");
        }

        // Check if customer already reviewed this vendor
        Optional<Review> existingReview = findByCustomerIdAndVendorId(customerId, request.getVendorId());
        if (existingReview.isPresent()) {
            throw new RuntimeException("You have already reviewed this vendor. Please edit your existing review instead.");
        }

        // Validate rating
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Create new review
        Review review = new Review();
        review.setId(generateNextId());
        review.setCustomer(customer.get());
        review.setVendor(vendor.get());
        review.setRating(request.getRating());
        review.setRatingScore((float) request.getRating());
        review.setReviewText(request.getReviewText());
        review.setCreatedAt(LocalDateTime.now());
        review.setIsApproved(true);
        review.setIsFlagged(false);

        // Save to file
        List<Review> reviews = readReviews();
        reviews.add(review);
        writeReviews(reviews);

        return mapToResponseDTO(review);
    }

    /**
     * Update existing review (only by the customer who created it, within 30 days)
     */
    public synchronized ReviewResponseDTO updateReview(Long customerId, Long reviewId, ReviewRequestDTO request) {
        Optional<Review> reviewOpt = findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();

        // Check ownership and 30-day edit window
        if (!review.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        LocalDateTime editDeadline = review.getCreatedAt().plusDays(30);
        if (LocalDateTime.now().isAfter(editDeadline)) {
            throw new RuntimeException("Reviews can only be edited within 30 days of creation");
        }

        // Update fields
        if (request.getRating() != null && request.getRating() >= 1 && request.getRating() <= 5) {
            review.setRating(request.getRating());
            review.setRatingScore((float) request.getRating());
        }

        if (request.getReviewText() != null && !request.getReviewText().isBlank()) {
            review.setReviewText(request.getReviewText());
        }

        review.setUpdatedAt(LocalDateTime.now());

        // Save to file
        List<Review> reviews = readReviews();
        reviews = reviews.stream()
                .map(r -> r.getId().equals(reviewId) ? review : r)
                .collect(Collectors.toList());
        writeReviews(reviews);

        return mapToResponseDTO(review);
    }

    /**
     * Delete review (only by owner)
     */
    public synchronized void deleteReview(Long customerId, Long reviewId) {
        Optional<Review> reviewOpt = findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();
        if (!review.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        List<Review> reviews = readReviews();
        reviews = reviews.stream()
                .filter(r -> !r.getId().equals(reviewId))
                .collect(Collectors.toList());
        writeReviews(reviews);
    }

    /**
     * Get all reviews for a vendor (public - only approved/not flagged)
     */
    public synchronized List<ReviewResponseDTO> getVendorReviews(Long vendorId) {
        return readReviews().stream()
                .filter(r -> r.getVendor().getId().equals(vendorId))
                .filter(r -> Boolean.TRUE.equals(r.getIsApproved()))
                .filter(r -> !Boolean.TRUE.equals(r.getIsFlagged()))
                .map(this::mapToResponseDTO)
                .sorted(Comparator.comparing(ReviewResponseDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get review summary for a vendor (average rating + distribution)
     */
    public synchronized Map<String, Object> getVendorReviewSummary(Long vendorId) {
        List<Review> vendorReviews = readReviews().stream()
                .filter(r -> r.getVendor().getId().equals(vendorId))
                .filter(r -> Boolean.TRUE.equals(r.getIsApproved()))
                .filter(r -> !Boolean.TRUE.equals(r.getIsFlagged()))
                .collect(Collectors.toList());

        if (vendorReviews.isEmpty()) {
            return Map.ofEntries(
                    Map.entry("vendorId", vendorId),
                    Map.entry("totalReviews", 0),
                    Map.entry("averageRating", 0.0),
                    Map.entry("distribution", Map.of("5", 0, "4", 0, "3", 0, "2", 0, "1", 0))
            );
        }

        double avgRating = vendorReviews.stream()
                .mapToDouble(r -> r.getRating() != null ? r.getRating() : 0)
                .average()
                .orElse(0.0);

        Map<Integer, Long> distribution = vendorReviews.stream()
                .collect(Collectors.groupingByConcurrent(Review::getRating, Collectors.counting()));

        Map<String, Integer> dist = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            dist.put(String.valueOf(i), distribution.getOrDefault(i, 0L).intValue());
        }

        return Map.ofEntries(
                Map.entry("vendorId", vendorId),
                Map.entry("totalReviews", vendorReviews.size()),
                Map.entry("averageRating", Math.round(avgRating * 10.0) / 10.0),
                Map.entry("distribution", dist)
        );
    }

    /**
     * Get reviews by the current customer
     */
    public synchronized List<ReviewResponseDTO> getMyReviews(Long customerId) {
        return readReviews().stream()
                .filter(r -> r.getCustomer().getId().equals(customerId))
                .map(this::mapToResponseDTO)
                .sorted(Comparator.comparing(ReviewResponseDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Vendor responds to a review
     */
    public synchronized ReviewResponseDTO addVendorReply(Long vendorId, Long reviewId, String reply) {
        Optional<Review> reviewOpt = findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();

        // Check vendor ownership
        if (!review.getVendor().getId().equals(vendorId)) {
            throw new RuntimeException("You can only reply to reviews of your own vendor");
        }

        review.setVendorResponse(reply);
        review.setVendorResponseAt(LocalDateTime.now());

        List<Review> reviews = readReviews();
        reviews = reviews.stream()
                .map(r -> r.getId().equals(reviewId) ? review : r)
                .collect(Collectors.toList());
        writeReviews(reviews);

        return mapToResponseDTO(review);
    }

    /**
     * Admin: Get all reviews for a vendor (including flagged/hidden)
     */
    public synchronized List<ReviewResponseDTO> getAllReviewsForVendorAdmin(Long vendorId) {
        return readReviews().stream()
                .filter(r -> r.getVendor().getId().equals(vendorId))
                .map(this::mapToResponseDTO)
                .sorted(Comparator.comparing(ReviewResponseDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Admin: Toggle review visibility
     */
    public synchronized ReviewResponseDTO toggleVisibility(Long reviewId) {
        Optional<Review> reviewOpt = findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();
        review.setIsApproved(!Boolean.TRUE.equals(review.getIsApproved()));

        List<Review> reviews = readReviews();
        reviews = reviews.stream()
                .map(r -> r.getId().equals(reviewId) ? review : r)
                .collect(Collectors.toList());
        writeReviews(reviews);

        return mapToResponseDTO(review);
    }

    /**
     * Admin: Flag review as inappropriate
     */
    public synchronized ReviewResponseDTO flagReview(Long reviewId, String reason) {
        Optional<Review> reviewOpt = findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();
        review.setIsFlagged(true);
        review.setFlagReason(reason);

        List<Review> reviews = readReviews();
        reviews = reviews.stream()
                .map(r -> r.getId().equals(reviewId) ? review : r)
                .collect(Collectors.toList());
        writeReviews(reviews);

        return mapToResponseDTO(review);
    }

    /**
     * Admin: Unflag review
     */
    public synchronized ReviewResponseDTO unflagReview(Long reviewId) {
        Optional<Review> reviewOpt = findById(reviewId);
        if (reviewOpt.isEmpty()) {
            throw new RuntimeException("Review not found");
        }

        Review review = reviewOpt.get();
        review.setIsFlagged(false);
        review.setFlagReason(null);

        List<Review> reviews = readReviews();
        reviews = reviews.stream()
                .map(r -> r.getId().equals(reviewId) ? review : r)
                .collect(Collectors.toList());
        writeReviews(reviews);

        return mapToResponseDTO(review);
    }

    // ─────────────────────────────────────────
    //  PRIVATE HELPERS
    // ─────────────────────────────────────────

    private Optional<Review> findById(Long reviewId) {
        return readReviews().stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst();
    }

    private Optional<Review> findByCustomerIdAndVendorId(Long customerId, Long vendorId) {
        return readReviews().stream()
                .filter(r -> r.getCustomer().getId().equals(customerId) && r.getVendor().getId().equals(vendorId))
                .findFirst();
    }

    private List<Review> readReviews() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }

            return Files.lines(path)
                    .filter(line -> !line.isBlank())
                    .map(this::fromJson)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read reviews from file", e);
        }
    }

    private void writeReviews(List<Review> reviews) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            List<String> lines = reviews.stream()
                    .map(this::toJson)
                    .collect(Collectors.toList());
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save reviews to file", e);
        }
    }

    private Review fromJson(String json) {
        try {
            return objectMapper.readValue(json, Review.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse review JSON", e);
        }
    }

    private String toJson(Review review) {
        try {
            return objectMapper.writeValueAsString(review);
        } catch (IOException e) {
            throw new RuntimeException("Unable to serialize review to JSON", e);
        }
    }

    private ReviewResponseDTO mapToResponseDTO(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setVendorId(review.getVendor().getId());
        dto.setVendorName(review.getVendor().getBusinessName());
        dto.setCustomerId(review.getCustomer().getId());
        dto.setCustomerName(maskName(review.getCustomer().getFullName()));
        dto.setRating(review.getRating());
        dto.setReviewText(review.getReviewText());
        dto.setVendorResponse(review.getVendorResponse());
        dto.setVendorResponseAt(review.getVendorResponseAt());
        dto.setIsApproved(review.getIsApproved());
        dto.setIsFlagged(review.getIsFlagged());
        dto.setFlagReason(review.getFlagReason());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }

    /** Mask customer name for privacy: "John Doe" → "John D." */
    private String maskName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return "Anonymous";
        }
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0];
        }
        return parts[0] + " " + parts[parts.length - 1].charAt(0) + ".";
    }

    private Long generateNextId() {
        List<Review> reviews = readReviews();
        return reviews.isEmpty() ? 1L : reviews.stream()
                .mapToLong(Review::getId)
                .max()
                .orElse(0L) + 1;
    }
}
