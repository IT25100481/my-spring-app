package com.example.my_spring_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String email, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Wedding Planner");
            message.setText("Welcome " + name + "!\n\nThank you for joining Wedding Planner. " +
                    "Start exploring vendors or create your profile to list your services.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
        }
    }

    public void sendVendorRegistrationEmail(String email, String businessName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Vendor Registration - Pending Approval");
            message.setText("Thank you for registering " + businessName + " as a vendor!\n\n" +
                    "Your profile is currently pending admin approval. " +
                    "We will review your details and notify you once approved.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending vendor registration email: " + e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String email, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset Successful");
            message.setText("Hello " + name + ",\n\nYour password has been successfully reset. " +
                    "If you did not request this, please contact support immediately.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
        }
    }

    public void sendVendorApprovalEmail(String email, String businessName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Vendor Profile Approved!");
            message.setText("Great news " + businessName + "!\n\n" +
                    "Your vendor profile has been approved. You can now login and " +
                    "start managing your services, bookings, and more!");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending vendor approval email: " + e.getMessage());
        }
    }

    public void sendVendorRejectionEmail(String email, String businessName, String reason) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Vendor Profile Status Update");
            message.setText("Hello " + businessName + ",\n\n" +
                    "Your vendor profile registration has been reviewed. " +
                    "Reason: " + reason + "\n\n" +
                    "Please contact support for more information.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending vendor rejection email: " + e.getMessage());
        }
    }

    public void sendBookingRequestNotification(String email, String vendorName, String eventDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("New Booking Request from " + vendorName);
            message.setText("You have a new booking request for event on " + eventDate + ". " +
                    "Please login to your dashboard to view and respond.");
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending booking notification email: " + e.getMessage());
        }
    }

    public void sendInvoiceEmail(String email, String invoiceNumber, String invoiceUrl) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Invoice #" + invoiceNumber);
            message.setText("Please find your invoice attached. " +
                    "Invoice Number: " + invoiceNumber + "\n" +
                    "Download: " + invoiceUrl);
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending invoice email: " + e.getMessage());
        }
    }
}
