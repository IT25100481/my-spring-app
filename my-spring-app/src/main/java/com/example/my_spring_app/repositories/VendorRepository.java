package com.example.my_spring_app.repositories;

import com.example.my_spring_app.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUser_Email(String email);
    List<Vendor> findByCategory(String category);
    List<Vendor> findByVerificationStatus(String status);
    List<Vendor> findByIsActiveTrue();
    List<Vendor> findByCategoryAndIsActiveTrue(String category);
    Optional<Vendor> findByBusinessName(String businessName);
}
