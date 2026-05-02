package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Review;
import com.example.my_spring_app.services.ReviewManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/reviews")
@CrossOrigin(origins = "*")
public class VendorReviewController {

    private final ReviewManagementService reviewManagementService;

    public VendorReviewController(ReviewManagementService reviewManagementService) {
        this.reviewManagementService = reviewManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@RequestParam String email) {
        return ResponseEntity.ok(reviewManagementService.getVendorReviews(email));
    }

    @PostMapping("/{reviewId}/respond")
    public ResponseEntity<?> respond(@RequestParam String email,
                                     @PathVariable Long reviewId,
                                     @RequestParam String response) {
        try {
            return ResponseEntity.ok(reviewManagementService.respondToReview(email, reviewId, response));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{reviewId}/flag")
    public ResponseEntity<?> flag(@RequestParam String email,
                                  @PathVariable Long reviewId,
                                  @RequestParam(required = false) String reason) {
        try {
            return ResponseEntity.ok(reviewManagementService.flagReview(email, reviewId, reason));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
