package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.BookingStatus;
import com.example.my_spring_app.models.Review;
import com.example.my_spring_app.repositories.BookingRepository;
import com.example.my_spring_app.repositories.ReviewRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final VendorRepository vendorRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentManagementService paymentManagementService;

    public AnalyticsService(VendorRepository vendorRepository,
                            BookingRepository bookingRepository,
                            ReviewRepository reviewRepository,
                            PaymentManagementService paymentManagementService) {
        this.vendorRepository = vendorRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.paymentManagementService = paymentManagementService;
    }

    public Map<String, Object> getDashboard(String email) {
        Vendor vendor = resolveVendor(email);
        List<Booking> bookings = bookingRepository.findByVendor_Id(vendor.getId());
        List<Review> reviews = reviewRepository.findByVendor_Id(vendor.getId());

        long pendingRequests = bookings.stream().filter(booking -> booking.getStatus() == BookingStatus.INQUIRY || booking.getStatus() == BookingStatus.QUOTED).count();
        long confirmedBookings = bookings.stream().filter(booking -> booking.getStatus() == BookingStatus.CONFIRMED).count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalBookings", bookings.size());
        response.put("pendingRequests", pendingRequests);
        response.put("confirmedBookings", confirmedBookings);
        response.put("monthlyEarnings", paymentManagementService.getEarningsSummary(email));
        response.put("profileViews", vendor.getProfileViews());
        response.put("reviewScoreAverage", vendor.getAverageRating());
        response.put("reviewCount", reviews.size());
        response.put("popularServices", vendor.getServices() == null ? List.of() : vendor.getServices());
        return response;
    }

    public Map<String, Object> getBookingTrends(String email) {
        Vendor vendor = resolveVendor(email);
        Map<String, Object> response = new HashMap<>();
        response.put("vendorId", vendor.getId());
        response.put("message", "Booking trend data can be extended with monthly aggregation.");
        return response;
    }

    public Map<String, Object> getRevenueTrends(String email) {
        Vendor vendor = resolveVendor(email);
        Map<String, Object> response = new HashMap<>();
        response.put("vendorId", vendor.getId());
        response.put("monthlyEarnings", paymentManagementService.getEarningsSummary(email));
        return response;
    }

    private Vendor resolveVendor(String email) {
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}
