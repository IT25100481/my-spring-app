package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.Payment;
import com.example.my_spring_app.models.PaymentMethod;
import com.example.my_spring_app.services.PaymentManagementService; // 1. Use the correct name here
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    // 2. Change the type to match your file: PaymentManagementService
    private final PaymentManagementService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<Payment> pay(@RequestParam Long bookingId,
                                       @RequestParam Double amount,
                                       @RequestParam PaymentMethod method) {
        // 3. Ensure this method name exists inside PaymentManagementService
        Payment processedPayment = paymentService.processNewPayment(bookingId, amount, method);
        return ResponseEntity.ok(processedPayment);
    }
}