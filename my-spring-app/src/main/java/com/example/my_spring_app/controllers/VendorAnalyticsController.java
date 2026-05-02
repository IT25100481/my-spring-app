package com.example.my_spring_app.controllers;

import com.example.my_spring_app.services.AnalyticsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/vendor/analytics")
@CrossOrigin(origins = "*")
public class VendorAnalyticsController {

    private final AnalyticsService analyticsService;

    public VendorAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@RequestParam String email) {
        return analyticsService.getDashboard(email);
    }

    @GetMapping("/bookings")
    public Map<String, Object> bookingTrends(@RequestParam String email) {
        return analyticsService.getBookingTrends(email);
    }

    @GetMapping("/revenue")
    public Map<String, Object> revenueTrends(@RequestParam String email) {
        return analyticsService.getRevenueTrends(email);
    }
}
