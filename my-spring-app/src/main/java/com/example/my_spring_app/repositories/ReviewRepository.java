package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Retrieves all reviews associated with a specific vendor's ID.
     * Essential for calculating average ratings and review counts in Analytics.
     */
    List<Review> findByVendor_Id(Long vendorId);

    /**
     * Optional: Finds reviews by rating (e.g., all 5-star reviews).
     */
    List<Review> findByRating(int rating);
}