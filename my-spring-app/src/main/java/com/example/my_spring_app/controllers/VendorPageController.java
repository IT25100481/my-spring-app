package com.example.my_spring_app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controller disabled because vendor page routing is handled by VendorDashboardController and templates are used for pages.
// Keeping this file for reference, but avoiding duplicated route mappings.
// @Controller
public class VendorPageController {

    @GetMapping("/vendor/profile")
    public String vendorProfilePage() {
        return "forward:/vendor-profile.html";
    }

    @GetMapping("/vendor/services")
    public String vendorServicesPage() {
        return "forward:/vendor-services.html";
    }

    @GetMapping("/vendor/bookings")
    public String vendorBookingsPage() {
        return "forward:/vendor-bookings.html";
    }

    @GetMapping("/vendor/availability")
    public String vendorAvailabilityPage() {
        return "forward:/vendor-availability.html";
    }

    @GetMapping("/vendor/messages")
    public String vendorMessagesPage() {
        return "forward:/vendor-messages.html";
    }

    @GetMapping("/vendor/reviews")
    public String vendorReviewsPage() {
        return "forward:/vendor-reviews.html";
    }

    @GetMapping("/vendor/notifications")
    public String vendorNotificationsPage() {
        return "forward:/vendor-notifications.html";
    }

    @GetMapping("/vendor/payments")
    public String vendorPaymentsPage() {
        return "forward:/vendor-payments.html";
    }

    @GetMapping("/vendor/analytics")
    public String vendorAnalyticsPage() {
        return "forward:/vendor-analytics.html";
    }
}
