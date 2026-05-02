package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private VendorService vendorService;

    public Map<String, Object> getDashboard(String email) {
        Vendor vendor = vendorService.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("totalBookings", 0);
        response.put("pendingRequests", 0);
        response.put("confirmedBookings", 0);
        response.put("monthlyEarnings", 0.0);
        response.put("profileViews", vendor.getProfileViews());
        response.put("reviewScoreAverage", vendor.getAverageRating());
        response.put("reviewCount", vendor.getTotalReviews());
        response.put("popularServices", vendor.getServices() == null ? java.util.List.of() : vendor.getServices());
        return response;
    }

    public Map<String, Object> getBookingTrends(String email) {
        Vendor vendor = vendorService.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        Map<String, Object> response = new HashMap<>();
        response.put("vendorId", vendor.getId());
        response.put("message", "Booking trend data can be extended with monthly aggregation.");
        return response;
    }

    public Map<String, Object> getRevenueTrends(String email) {
        Vendor vendor = vendorService.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        Map<String, Object> response = new HashMap<>();
        response.put("vendorId", vendor.getId());
        response.put("monthlyEarnings", 0.0);
        return response;
    }
}
