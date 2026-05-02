package com.example.my_spring_app.services;

import com.example.my_spring_app.models.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class BookingManagementService {

    public List<Booking> getVendorBookings(String email) {
        return Collections.emptyList();
    }

    public Booking getBooking(String email, Long bookingId) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public Booking acceptBooking(String email, Long bookingId) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public Booking declineBooking(String email, Long bookingId, String reason) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public Booking negotiateBooking(String email, Long bookingId, Double quotedPrice, String notes) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public Booking confirmBooking(String email, Long bookingId) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public Booking completeBooking(String email, Long bookingId) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public Booking cancelBooking(String email, Long bookingId, String reason) {
        throw new UnsupportedOperationException("Booking operations are not available with text-file persistence yet.");
    }

    public List<Booking> getUpcomingBookings(String email, LocalDate startDate, LocalDate endDate) {
        return Collections.emptyList();
    }
}
