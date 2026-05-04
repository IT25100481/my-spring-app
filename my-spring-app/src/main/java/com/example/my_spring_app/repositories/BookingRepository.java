package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Fixes: java: cannot find symbol method findByVendor_Id(java.lang.Long)
     */
    List<Booking> findByVendor_Id(Long vendorId);

    /**
     * Keep this one from the previous step for date filtering
     */
    List<Booking> findByVendor_IdAndEventDateBetween(Long vendorId, LocalDate startDate, LocalDate endDate);

    /**
     * Helper to find by vendor email
     */
    List<Booking> findByVendor_User_Email(String email);
}