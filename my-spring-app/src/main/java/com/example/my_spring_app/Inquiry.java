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

    private String subject;

    private String message;

    private String inquiryType; // e.g., "wedding", "vendor", "general"

    private String status = "pending"; // pending, responded, closed

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
        this.createdAt = LocalDateTime.now().toString();
    }

    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }
}