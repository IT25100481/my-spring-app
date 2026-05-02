package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.BookingStatus;
import com.example.my_spring_app.models.PaymentStatus;
import com.example.my_spring_app.repositories.BookingRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingManagementService {

    private final VendorRepository vendorRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public BookingManagementService(VendorRepository vendorRepository,
                                     BookingRepository bookingRepository,
                                     EmailService emailService) {
        this.vendorRepository = vendorRepository;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    public List<Booking> getVendorBookings(String email) {
        Vendor vendor = resolveVendor(email);
        return bookingRepository.findByVendor_Id(vendor.getId());
    }

    public Booking getBooking(String email, Long bookingId) {
        return getOwnedBooking(email, bookingId);
    }

    public Booking acceptBooking(String email, Long bookingId) {
        Booking booking = getOwnedBooking(email, bookingId);
        ensureCanTransition(booking);
        booking.setStatus(BookingStatus.ACCEPTED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking declineBooking(String email, Long bookingId, String reason) {
        Booking booking = getOwnedBooking(email, bookingId);
        ensureCanTransition(booking);
        booking.setStatus(BookingStatus.DECLINED);
        booking.setNotes(appendNote(booking.getNotes(), "Declined: " + reason));
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking negotiateBooking(String email, Long bookingId, Double quotedPrice, String notes) {
        Booking booking = getOwnedBooking(email, bookingId);
        booking.setStatus(BookingStatus.QUOTED);
        if (quotedPrice != null) booking.setQuotedPrice(quotedPrice);
        booking.setNotes(appendNote(booking.getNotes(), notes));
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(String email, Long bookingId) {
        Booking booking = getOwnedBooking(email, bookingId);
        ensureDateAvailable(booking.getVendor().getId(), booking.getEventDate(), bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedDate(LocalDate.now());
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking completeBooking(String email, Long bookingId) {
        Booking booking = getOwnedBooking(email, bookingId);
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(String email, Long bookingId, String reason) {
        Booking booking = getOwnedBooking(email, bookingId);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setNotes(appendNote(booking.getNotes(), "Cancelled: " + reason));
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getUpcomingBookings(String email, LocalDate startDate, LocalDate endDate) {
        Vendor vendor = resolveVendor(email);
        return bookingRepository.findByVendor_IdAndEventDateBetween(vendor.getId(), startDate, endDate);
    }

    private void ensureDateAvailable(Long vendorId, LocalDate eventDate, Long ignoreBookingId) {
        List<Booking> sameDate = bookingRepository.findByVendor_IdAndEventDateBetween(vendorId, eventDate, eventDate);
        boolean conflict = sameDate.stream()
                .anyMatch(existing -> !existing.getId().equals(ignoreBookingId)
                        && existing.getStatus() == BookingStatus.CONFIRMED);
        if (conflict) {
            throw new RuntimeException("This date is already booked");
        }
    }

    private void ensureCanTransition(Booking booking) {
        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking can no longer be updated");
        }
    }

    private Booking getOwnedBooking(String email, Long bookingId) {
        Vendor vendor = resolveVendor(email);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this booking");
        }
        return booking;
    }

    private Vendor resolveVendor(String email) {
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    private String appendNote(String original, String next) {
        if (next == null || next.isBlank()) return original;
        if (original == null || original.isBlank()) return next.trim();
        return original + "\n" + next.trim();
    }
}
