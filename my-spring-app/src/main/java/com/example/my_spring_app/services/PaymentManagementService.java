package com.example.my_spring_app.services;

import com.example.my_spring_app.models.Invoice;
import com.example.my_spring_app.models.Payment;
import com.example.my_spring_app.models.PaymentMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class PaymentManagementService {

    public List<Payment> getPayments(String email) {
        return Collections.emptyList();
    }

    public List<Invoice> getInvoices(String email) {
        return Collections.emptyList();
    }

    public Invoice generateInvoice(String email, Long bookingId, Double subtotal, Double taxPercentage, LocalDate dueDate) {
        throw new UnsupportedOperationException("Payments and invoices are not available with text-file persistence yet.");
    }

    public Payment recordPayment(String email, Long bookingId, Double amountPaid, PaymentMethod method, String transactionId) {
        throw new UnsupportedOperationException("Payments and invoices are not available with text-file persistence yet.");
    }

    public Double getEarningsSummary(String email) {
        return 0.0;
    }
}
