package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVendor_Id(Long vendorId);
    List<Review> findByCustomer_Id(Long customerId);
    List<Review> findByVendor_IdAndIsApprovedTrue(Long vendorId);
    List<Review> findByVendor_IdAndIsFlaggedTrue(Long vendorId);
    int countByVendor_Id(Long vendorId);
}
