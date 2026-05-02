package com.example.my_spring_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendorDashboardController {

    @GetMapping("/vendor/dashboard")
    public String vendorDashboard() {
        return "forward:/vendor-dashboard.html";
    }

    @GetMapping("/vendor/dashboard/partd")
    public String vendorDashboardPartd() {
        return "forward:/vendor-dashboard-partd.html";
    }

    @GetMapping("/vendor/profile")
    public String vendorProfile() {
        return "forward:/vendor-profile.html";
    }

    @GetMapping("/vendor/services")
    public String vendorServices() {
        return "forward:/vendor-services.html";
    }

    @GetMapping("/vendor/bookings")
    public String vendorBookings() {
        return "forward:/vendor-bookings.html";
    }

    @GetMapping("/vendor/payments")
    public String vendorPayments() {
        return "forward:/vendor-payments.html";
    }

    @GetMapping("/vendor/availability")
    public String vendorAvailability() {
        return "forward:/vendor-availability.html";
    }

    @GetMapping("/vendor/messages")
    public String vendorMessages() {
        return "forward:/vendor-messages.html";
    }

    @GetMapping("/vendor/reviews")
    public String vendorReviews() {
        return "forward:/vendor-reviews.html";
    }

    @GetMapping("/vendor/notifications")
    public String vendorNotifications() {
        return "forward:/vendor-notifications.html";
    }

    @GetMapping("/vendor/analytics")
    public String vendorAnalytics() {
        return "forward:/vendor-analytics.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login.html";
    }
}
