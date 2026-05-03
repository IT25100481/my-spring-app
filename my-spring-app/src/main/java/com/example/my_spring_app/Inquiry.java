package com.example.my_spring_app;

import lombok.*;
import java.time.LocalDateTime;

// Inquiry model for contact/inquiry forms
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    private Long id;

    private String name;

    private String email;

    private String phone;

    // Enhanced fields
    private String weddingDate; // Preferred wedding date
    private Integer guestCount; // Number of guests
    private String budgetRange; // e.g., "50k-100k", "100k-200k", "200k+"
    private String preferredLocation; // Preferred wedding location
    private String servicesNeeded; // Comma-separated services (photography, catering, etc.)

    private String subject;

    private String message;

    private String inquiryType; // e.g., "wedding", "vendor", "general"

    private String status = "pending"; // pending, in-progress, responded, completed, cancelled

    private String priority = "normal"; // low, normal, high, urgent

    private String assignedTo; // Admin/vendor assigned to handle this inquiry

    private String adminNotes; // Internal notes from admin

    private String responseMessage; // Admin's response to the inquiry

    private String createdAt;

    private String updatedAt;

    public Inquiry(String name, String email, String phone, String subject, String message, String inquiryType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.message = message;
        this.inquiryType = inquiryType;
        this.status = "pending";
        this.priority = "normal";
        this.createdAt = LocalDateTime.now().toString();
    }

    // Enhanced constructor for detailed inquiries
    public Inquiry(String name, String email, String phone, String weddingDate, Integer guestCount,
                   String budgetRange, String preferredLocation, String servicesNeeded,
                   String subject, String message, String inquiryType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.weddingDate = weddingDate;
        this.guestCount = guestCount;
        this.budgetRange = budgetRange;
        this.preferredLocation = preferredLocation;
        this.servicesNeeded = servicesNeeded;
        this.subject = subject;
        this.message = message;
        this.inquiryType = inquiryType;
        this.status = "pending";
        this.priority = "normal";
        this.createdAt = LocalDateTime.now().toString();
    }

    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }
}