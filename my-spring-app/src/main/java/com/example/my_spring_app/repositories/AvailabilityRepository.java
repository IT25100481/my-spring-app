package com.example.my_spring_app.repositories;

import com.example.my_spring_app.models.Availability;
import com.example.my_spring_app.models.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByVendor_Id(Long vendorId);
    List<Availability> findByVendor_IdAndDateAvailable(Long vendorId, LocalDate date);
    List<Availability> findByVendor_IdAndStatus(Long vendorId, AvailabilityStatus status);
    List<Availability> findByDateAvailableBetween(LocalDate startDate, LocalDate endDate);
}
