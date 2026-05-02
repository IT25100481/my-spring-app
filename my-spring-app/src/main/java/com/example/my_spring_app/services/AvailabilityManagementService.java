package com.example.my_spring_app.services;

import com.example.my_spring_app.models.Availability;
import com.example.my_spring_app.models.AvailabilityStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class AvailabilityManagementService {

    public List<Availability> getAvailability(String email) {
        return Collections.emptyList();
    }

    public Availability setAvailability(String email, LocalDate date, AvailabilityStatus status,
                                        LocalTime startTime, LocalTime endTime, String reason) {
        throw new UnsupportedOperationException("Availability management is not available with text-file persistence yet.");
    }

    public Availability blockDate(String email, LocalDate date, String reason) {
        throw new UnsupportedOperationException("Availability management is not available with text-file persistence yet.");
    }

    public Availability unblockDate(String email, LocalDate date) {
        throw new UnsupportedOperationException("Availability management is not available with text-file persistence yet.");
    }

    public List<Availability> getCalendar(String email, LocalDate startDate, LocalDate endDate) {
        return Collections.emptyList();
    }

    public void autoBlockConfirmedBooking(Object booking) {
        // Not supported in file-based persistence mode
    }

    public Availability setWorkingHours(String email, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        throw new UnsupportedOperationException("Availability management is not available with text-file persistence yet.");
    }
}
