package com.example.my_spring_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // Submit an inquiry
    @PostMapping("/inquiry")
    public ResponseEntity<Map<String, String>> submitInquiry(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam(defaultValue = "general") String inquiryType) {

        try {
            // Save inquiry
            Inquiry inquiry = new Inquiry(name, email, phone, subject, message, inquiryType);
            inquiryService.saveInquiry(inquiry);

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Thank you for your inquiry! We'll get back to you soon."
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to submit inquiry. Please try again."));
        }
    }

    // Get all inquiries (for admin purposes)
    @GetMapping("/inquiries")
    public ResponseEntity<?> getAllInquiries() {
        try {
            return ResponseEntity.ok(inquiryService.getAllInquiries());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve inquiries."));
        }
    }

    // Get pending inquiries
    @GetMapping("/inquiries/pending")
    public ResponseEntity<?> getPendingInquiries() {
        try {
            return ResponseEntity.ok(inquiryService.getPendingInquiries());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve pending inquiries."));
        }
    }
}