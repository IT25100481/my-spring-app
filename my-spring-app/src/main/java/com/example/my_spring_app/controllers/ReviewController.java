package com.example.my_spring_app.controllers;

import com.example.my_spring_app.dtos.ReviewRequestDTO;
import com.example.my_spring_app.dtos.ReviewResponseDTO;
import com.example.my_spring_app.security.JwtTokenProvider;
import com.example.my_spring_app.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Review Management REST Controller
 *
 * Public endpoints:
 *   GET  /api/reviews/vendor/{vendorId}          – list vendor reviews
 *   GET  /api/reviews/vendor/{vendorId}/summary  – average rating + distribution
 *
 * Customer endpoints (requires JWT):
 *   POST   /api/reviews                          – submit review
 *   PUT    /api/reviews/{reviewId}               – edit own review
 *   DELETE /api/reviews/{reviewId}               – delete own review
 *   GET    /api/reviews/my-reviews               – view own reviews
 *
 * Vendor endpoints (requires JWT):
 *   POST   /api/reviews/{reviewId}/reply         – reply to review
 *
 * Admin endpoints (requires JWT):
 *   GET    /api/reviews/admin/vendor/{vendorId}  – all reviews incl. flagged
 *   PATCH  /api/reviews/admin/{reviewId}/toggle  – hide/show review
 *   PATCH  /api/reviews/admin/{reviewId}/flag    – flag review as inappropriate
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // ─────────────────────────────────────────
    //  PUBLIC ENDPOINTS
    // ─────────────────────────────────────────

    /**
     * Get all approved reviews for a vendor
     */
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorReviews(@PathVariable Long vendorId) {
        try {
            List<ReviewResponseDTO> reviews = reviewService.getVendorReviews(vendorId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", reviews.size(),
                    "reviews", reviews
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Get review summary: average rating + distribution
     */
    @GetMapping("/vendor/{vendorId}/summary")
    public ResponseEntity<?> getVendorReviewSummary(@PathVariable Long vendorId) {
        try {
            Map<String, Object> summary = reviewService.getVendorReviewSummary(vendorId);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    // ─────────────────────────────────────────
    //  CUSTOMER ENDPOINTS
    // ─────────────────────────────────────────

    /**
     * Submit a new review (customer)
     */
    @PostMapping
    public ResponseEntity<?> submitReview(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody ReviewRequestDTO request) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            Long customerId = extractUserIdFromToken(authHeader);
            ReviewResponseDTO review = reviewService.submitReview(customerId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Review submitted successfully",
                    "review", review
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Update own review (customer)
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDTO request) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            Long customerId = extractUserIdFromToken(authHeader);
            ReviewResponseDTO review = reviewService.updateReview(customerId, reviewId, request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Review updated successfully",
                    "review", review
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Delete own review (customer)
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long reviewId) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            Long customerId = extractUserIdFromToken(authHeader);
            reviewService.deleteReview(customerId, reviewId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Review deleted successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Get my reviews (customer)
     */
    @GetMapping("/my-reviews")
    public ResponseEntity<?> getMyReviews(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            Long customerId = extractUserIdFromToken(authHeader);
            List<ReviewResponseDTO> reviews = reviewService.getMyReviews(customerId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", reviews.size(),
                    "reviews", reviews
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    // ─────────────────────────────────────────
    //  VENDOR ENDPOINTS
    // ─────────────────────────────────────────

    /**
     * Vendor replies to a review
     */
    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<?> replyToReview(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long reviewId,
            @RequestBody Map<String, String> body) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            String reply = body.get("reply");
            if (reply == null || reply.isBlank()) {
                return ResponseEntity.badRequest().body(errorResponse("Reply cannot be empty"));
            }

            Long vendorId = extractUserIdFromToken(authHeader);
            ReviewResponseDTO review = reviewService.addVendorReply(vendorId, reviewId, reply);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Reply added successfully",
                    "review", review
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    // ─────────────────────────────────────────
    //  ADMIN ENDPOINTS
    // ─────────────────────────────────────────

    /**
     * Admin: Get all reviews for a vendor (including flagged)
     */
    @GetMapping("/admin/vendor/{vendorId}")
    public ResponseEntity<?> getAllReviewsAdmin(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long vendorId) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            // In a real app, verify admin role - for now we trust the token
            List<ReviewResponseDTO> reviews = reviewService.getAllReviewsForVendorAdmin(vendorId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", reviews.size(),
                    "reviews", reviews
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Admin: Toggle review visibility
     */
    @PatchMapping("/admin/{reviewId}/toggle")
    public ResponseEntity<?> toggleReviewVisibility(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long reviewId) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            ReviewResponseDTO review = reviewService.toggleVisibility(reviewId);
            String status = Boolean.TRUE.equals(review.getIsApproved()) ? "shown" : "hidden";
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Review is now " + status,
                    "review", review
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Admin: Flag review as inappropriate
     */
    @PatchMapping("/admin/{reviewId}/flag")
    public ResponseEntity<?> flagReview(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long reviewId,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            String reason = (body != null) ? body.get("reason") : null;
            ReviewResponseDTO review = reviewService.flagReview(reviewId, reason);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Review flagged as inappropriate",
                    "review", review
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    /**
     * Admin: Unflag review
     */
    @PatchMapping("/admin/{reviewId}/unflag")
    public ResponseEntity<?> unflagReview(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long reviewId) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(errorResponse("Authorization header missing or invalid"));
            }

            ReviewResponseDTO review = reviewService.unflagReview(reviewId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Review unflagged",
                    "review", review
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(errorResponse(e.getMessage()));
        }
    }

    // ─────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────

    private Long extractUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    private Map<String, Object> errorResponse(String message) {
        return Map.of(
                "success", false,
                "message", message
        );
    }
}
