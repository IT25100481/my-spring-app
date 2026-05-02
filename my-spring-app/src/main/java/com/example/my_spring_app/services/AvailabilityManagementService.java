package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.models.Availability;
import com.example.my_spring_app.models.AvailabilityStatus;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.BookingStatus;
import com.example.my_spring_app.repositories.AvailabilityRepository;
import com.example.my_spring_app.repositories.BookingRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityManagementService {

    private final VendorRepository vendorRepository;
    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;

    public AvailabilityManagementService(VendorRepository vendorRepository,
                                         AvailabilityRepository availabilityRepository,
                                         BookingRepository bookingRepository) {
        this.vendorRepository = vendorRepository;
        this.availabilityRepository = availabilityRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Availability> getAvailability(String email) {
        Vendor vendor = resolveVendor(email);
        return availabilityRepository.findByVendor_Id(vendor.getId());
    }

    public Availability setAvailability(String email, LocalDate date, AvailabilityStatus status,
                                        LocalTime startTime, LocalTime endTime, String reason) {
        Vendor vendor = resolveVendor(email);
        Availability availability = availabilityRepository
                .findByVendor_IdAndDateAvailable(vendor.getId(), date)
                .stream().findFirst()
                .orElseGet(Availability::new);

        availability.setVendor(vendor);
        availability.setDateAvailable(date);
        availability.setDayOfWeek(date.getDayOfWeek());
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setStatus(status);
        availability.setReason(reason);
        availability.setUpdatedAt(LocalDateTime.now());
        return availabilityRepository.save(availability);
    }

    public Availability blockDate(String email, LocalDate date, String reason) {
        return setAvailability(email, date, AvailabilityStatus.BLOCKED, null, null, reason);
    }

    public Availability unblockDate(String email, LocalDate date) {
        return setAvailability(email, date, AvailabilityStatus.AVAILABLE, null, null, null);
    }

    public List<Availability> getCalendar(String email, LocalDate startDate, LocalDate endDate) {
        Vendor vendor = resolveVendor(email);
        return availabilityRepository.findByDateAvailableBetween(startDate, endDate).stream()
                .filter(item -> item.getVendor().getId().equals(vendor.getId()))
                .toList();
    }

    public void autoBlockConfirmedBooking(Booking booking) {
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            return;
        }
        blockDate(booking.getVendor().getUser().getEmail(), booking.getEventDate(), "Auto-blocked by confirmed booking");
    }

    public Availability setWorkingHours(String email, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        Vendor vendor = resolveVendor(email);
        Availability availability = new Availability();
        availability.setVendor(vendor);
        availability.setDayOfWeek(dayOfWeek);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setStatus(AvailabilityStatus.AVAILABLE);
        availability.setReason("Working hours");
        availability.setUpdatedAt(LocalDateTime.now());
        return availabilityRepository.save(availability);
    }

    private Vendor resolveVendor(String email) {
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}
