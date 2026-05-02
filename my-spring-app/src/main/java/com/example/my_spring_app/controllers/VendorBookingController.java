package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.services.AvailabilityManagementService;
import com.example.my_spring_app.services.BookingManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/bookings")
@CrossOrigin(origins = "*")
public class VendorBookingController {

    private final BookingManagementService bookingManagementService;
    private final AvailabilityManagementService availabilityManagementService;

    public VendorBookingController(BookingManagementService bookingManagementService,
                                   AvailabilityManagementService availabilityManagementService) {
        this.bookingManagementService = bookingManagementService;
        this.availabilityManagementService = availabilityManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(@RequestParam String email) {
        return ResponseEntity.ok(bookingManagementService.getVendorBookings(email));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBooking(@RequestParam String email, @PathVariable Long bookingId) {
        try {
            return ResponseEntity.ok(bookingManagementService.getBooking(email, bookingId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/accept")
    public ResponseEntity<?> accept(@RequestParam String email, @PathVariable Long bookingId) {
        try {
            Booking booking = bookingManagementService.acceptBooking(email, bookingId);
            availabilityManagementService.autoBlockConfirmedBooking(booking);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/decline")
    public ResponseEntity<?> decline(@RequestParam String email,
                                     @PathVariable Long bookingId,
                                     @RequestParam(required = false) String reason) {
        try {
            return ResponseEntity.ok(bookingManagementService.declineBooking(email, bookingId, reason));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/negotiate")
    public ResponseEntity<?> negotiate(@RequestParam String email,
                                       @PathVariable Long bookingId,
                                       @RequestParam(required = false) Double quotedPrice,
                                       @RequestParam(required = false) String notes) {
        try {
            return ResponseEntity.ok(bookingManagementService.negotiateBooking(email, bookingId, quotedPrice, notes));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<?> confirm(@RequestParam String email, @PathVariable Long bookingId) {
        try {
            Booking booking = bookingManagementService.confirmBooking(email, bookingId);
            availabilityManagementService.autoBlockConfirmedBooking(booking);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/complete")
    public ResponseEntity<?> complete(@RequestParam String email, @PathVariable Long bookingId) {
        try {
            return ResponseEntity.ok(bookingManagementService.completeBooking(email, bookingId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancel(@RequestParam String email,
                                    @PathVariable Long bookingId,
                                    @RequestParam(required = false) String reason) {
        try {
            return ResponseEntity.ok(bookingManagementService.cancelBooking(email, bookingId, reason));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Booking>> upcoming(@RequestParam String email,
                                                  @RequestParam String startDate,
                                                  @RequestParam String endDate) {
        return ResponseEntity.ok(bookingManagementService.getUpcomingBookings(email, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
