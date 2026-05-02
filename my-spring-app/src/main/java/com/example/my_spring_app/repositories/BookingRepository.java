package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByVendor_Id(Long vendorId);
    List<Booking> findByCustomer_Id(Long customerId);
    List<Booking> findByVendor_IdAndStatus(Long vendorId, BookingStatus status);
    List<Booking> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    List<Booking> findByVendor_IdAndEventDateBetween(Long vendorId, LocalDate startDate, LocalDate endDate);
    int countByVendor_IdAndStatus(Long vendorId, BookingStatus status);
}
