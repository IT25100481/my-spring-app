package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBooking_Id(Long bookingId);
    List<Payment> findByVendor_Id(Long vendorId);
    List<Payment> findByVendor_IdAndPaymentStatus(Long vendorId, String status);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByVendor_IdOrderByCreatedAtDesc(Long vendorId);
}
