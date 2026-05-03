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

    // Submit an inquiry (enhanced with more fields)
    @PostMapping("/inquiry")
    public ResponseEntity<Map<String, String>> submitInquiry(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String weddingDate,
            @RequestParam(required = false) Integer guestCount,
            @RequestParam(required = false) String budgetRange,
            @RequestParam(required = false) String preferredLocation,
            @RequestParam(required = false) String servicesNeeded,
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam(defaultValue = "general") String inquiryType) {

        try {
            // Create enhanced inquiry
            Inquiry inquiry = new Inquiry(name, email, phone, weddingDate, guestCount,
                                        budgetRange, preferredLocation, servicesNeeded,
                                        subject, message, inquiryType);
            inquiryService.saveInquiry(inquiry);

            return ResponseEntity.ok(Map.of(
                "status", "success",
                "inquiryId", String.valueOf(inquiry.getId()),
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

    // Get inquiries by status
    @GetMapping("/inquiries/status/{status}")
    public ResponseEntity<?> getInquiriesByStatus(@PathVariable String status) {
        try {
            return ResponseEntity.ok(inquiryService.getInquiriesByStatus(status));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve inquiries."));
        }
    }

    // Get inquiries by type
    @GetMapping("/inquiries/type/{type}")
    public ResponseEntity<?> getInquiriesByType(@PathVariable String type) {
        try {
            return ResponseEntity.ok(inquiryService.getInquiriesByType(type));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve inquiries."));
        }
    }

    // Customer can check their own inquiries and admin replies by email
    @GetMapping("/inquiries/customer")
    public ResponseEntity<?> getCustomerInquiries(@RequestParam String email) {
        try {
            return ResponseEntity.ok(inquiryService.getInquiriesByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve customer inquiries."));
        }
    }

    // Search inquiries
    @GetMapping("/inquiries/search")
    public ResponseEntity<?> searchInquiries(@RequestParam String q) {
        try {
            return ResponseEntity.ok(inquiryService.searchInquiries(q));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to search inquiries."));
        }
    }

    // Update inquiry status
    @PutMapping("/inquiries/{id}/status")
    public ResponseEntity<Map<String, String>> updateInquiryStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String adminNotes) {
        try {
            boolean updated = inquiryService.updateInquiryStatus(id, status, adminNotes);
            if (updated) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Inquiry status updated successfully."
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to update inquiry status."));
        }
    }

    // Assign inquiry to admin/vendor
    @PutMapping("/inquiries/{id}/assign")
    public ResponseEntity<Map<String, String>> assignInquiry(
            @PathVariable Long id,
            @RequestParam String assignedTo) {
        try {
            boolean assigned = inquiryService.assignInquiry(id, assignedTo);
            if (assigned) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Inquiry assigned successfully."
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to assign inquiry."));
        }
    }

    // Respond to inquiry
    @PutMapping("/inquiries/{id}/respond")
    public ResponseEntity<Map<String, String>> respondToInquiry(
            @PathVariable Long id,
            @RequestParam String responseMessage) {
        try {
            boolean responded = inquiryService.respondToInquiry(id, responseMessage);
            if (responded) {
                return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Response sent successfully."
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to send response."));
        }
    }

    // Get inquiry statistics
    @GetMapping("/inquiries/statistics")
    public ResponseEntity<?> getInquiryStatistics() {
        try {
            return ResponseEntity.ok(inquiryService.getInquiryStatistics());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve statistics."));
        }
    }

    // Get single inquiry by ID
    @GetMapping("/inquiries/{id}")
    public ResponseEntity<?> getInquiryById(@PathVariable Long id) {
        try {
            return inquiryService.findById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error",
                            "message", "Failed to retrieve inquiry."));
        }
    }
}
