package com.example.my_spring_app.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendWelcomeEmail(String email, String name) {
        // Stub implementation - no email sending
        System.out.println("Would send welcome email to " + email + " for user " + name);
    }

    public void sendVendorRegistrationEmail(String email, String businessName) {
        // Stub implementation - no email sending
        System.out.println("Would send vendor registration email to " + email + " for business " + businessName);
    }

    public void sendPasswordResetEmail(String email, String name) {
        // Stub implementation - no email sending
        System.out.println("Would send password reset email to " + email + " for user " + name);
    }

    public void sendVendorApprovalEmail(String email, String businessName) {
        // Stub implementation - no email sending
        System.out.println("Would send vendor approval email to " + email + " for business " + businessName);
    }

    public void sendVendorRejectionEmail(String email, String businessName, String reason) {
        // Stub implementation - no email sending
        System.out.println("Would send vendor rejection email to " + email + " for business " + businessName + " with reason: " + reason);
    }

    public void sendBookingRequestNotification(String email, String vendorName, String eventDate) {
        // Stub implementation - no email sending
        System.out.println("Would send booking notification to " + email + " for " + vendorName + " on " + eventDate);
    }

    public void sendInvoiceEmail(String email, String invoiceNumber, String invoiceUrl) {
        // Stub implementation - no email sending
        System.out.println("Would send invoice " + invoiceNumber + " to " + email + " at " + invoiceUrl);
    }
}
