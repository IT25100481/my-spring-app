package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Invoice;
import com.example.my_spring_app.models.Payment;
import com.example.my_spring_app.models.PaymentMethod;
import com.example.my_spring_app.services.PaymentManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor/payments")
@CrossOrigin(origins = "*")
public class VendorPaymentController {

    private final PaymentManagementService paymentManagementService;

    public VendorPaymentController(PaymentManagementService paymentManagementService) {
        this.paymentManagementService = paymentManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getPayments(@RequestParam String email) {
        return ResponseEntity.ok(paymentManagementService.getPayments(email));
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getInvoices(@RequestParam String email) {
        return ResponseEntity.ok(paymentManagementService.getInvoices(email));
    }

    @PostMapping("/invoices")
    public ResponseEntity<?> generateInvoice(@RequestParam String email,
                                             @RequestParam Long bookingId,
                                             @RequestParam Double subtotal,
                                             @RequestParam(required = false) Double taxPercentage,
                                             @RequestParam(required = false) String dueDate) {
        try {
            LocalDate parsedDueDate = dueDate == null || dueDate.isBlank() ? null : LocalDate.parse(dueDate);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentManagementService.generateInvoice(email, bookingId, subtotal, taxPercentage, parsedDueDate));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @PostMapping("/record")
    public ResponseEntity<?> recordPayment(@RequestParam String email,
                                           @RequestParam Long bookingId,
                                           @RequestParam Double amountPaid,
                                           @RequestParam PaymentMethod paymentMethod,
                                           @RequestParam(required = false) String transactionId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentManagementService.recordPayment(email, bookingId, amountPaid, paymentMethod, transactionId));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error(ex.getMessage()));
        }
    }

    @GetMapping("/earnings")
    public ResponseEntity<Map<String, Double>> earnings(@RequestParam String email) {
        Map<String, Double> response = new HashMap<>();
        response.put("earnings", paymentManagementService.getEarningsSummary(email));
        return ResponseEntity.ok(response);
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }
}
