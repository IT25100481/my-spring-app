package com.example.my_spring_app.services;

import com.example.my_spring_app.models.*;
import com.example.my_spring_app.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentManagementService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final VendorRepository vendorRepository;
    private final InvoiceRepository invoiceRepository;

    /**
     * Fixes: java: cannot find symbol method getEarningsSummary(java.lang.String)
     */
    public Double getEarningsSummary(String email) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getVendor() != null &&
                        p.getVendor().getUser() != null &&
                        p.getVendor().getUser().getEmail().equalsIgnoreCase(email))
                .mapToDouble(Payment::getAmountPaid)
                .sum();
    }

    /**
     * Fixes: java: cannot find symbol method getPayments(java.lang.String)
     */
    public List<Payment> getPayments(String email) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getVendor() != null &&
                        p.getVendor().getUser() != null &&
                        p.getVendor().getUser().getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    /**
     * Fixes: java: cannot find symbol method getInvoices(java.lang.String)
     */
    public List<Invoice> getInvoices(String email) {
        return invoiceRepository.findAllInvoicesByEmail(email);
    }

    /**
     * Logic for generating new invoices
     */
    @Transactional
    public Invoice generateInvoice(String email, Long bookingId, Double subtotal, Double tax, LocalDate dueDate) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        double taxMultiplier = (tax != null) ? (tax / 100) : 0;
        double totalAmount = subtotal + (subtotal * taxMultiplier);

        Invoice invoice = new Invoice();
        invoice.setBooking(booking);
        invoice.setAmount(totalAmount);
        invoice.setDueDate(dueDate != null ? dueDate : LocalDate.now().plusDays(7));
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return invoiceRepository.save(invoice);
    }

    /**
     * Fixes: java: cannot find symbol method recordPayment(...)
     */
    @Transactional
    public Payment recordPayment(String email, Long bookingId, Double amount, PaymentMethod method, String txnId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setVendor(booking.getVendor());
        payment.setAmountPaid(amount);
        payment.setPaymentMethod(method);
        payment.setTransactionId(txnId != null && !txnId.isBlank() ? txnId : "MANUAL-" + UUID.randomUUID().toString().substring(0,8));
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Fixes: java: cannot find symbol method processNewPayment(...)
     * Often used by automated tasks or other services.
     */
    @Transactional
    public Payment processNewPayment(Long bookingId, Double amount, PaymentMethod method) {
        return recordPayment(null, bookingId, amount, method, null);
    }
}