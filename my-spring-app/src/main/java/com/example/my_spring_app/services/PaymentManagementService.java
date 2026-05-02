package com.example.my_spring_app.services;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.models.Booking;
import com.example.my_spring_app.models.Invoice;
import com.example.my_spring_app.models.InvoiceStatus;
import com.example.my_spring_app.models.Payment;
import com.example.my_spring_app.models.PaymentMethod;
import com.example.my_spring_app.models.PaymentStatus;
import com.example.my_spring_app.repositories.BookingRepository;
import com.example.my_spring_app.repositories.InvoiceRepository;
import com.example.my_spring_app.repositories.PaymentRepository;
import com.example.my_spring_app.repositories.UserRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentManagementService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final BookingRepository bookingRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public PaymentManagementService(PaymentRepository paymentRepository,
                                    InvoiceRepository invoiceRepository,
                                    BookingRepository bookingRepository,
                                    VendorRepository vendorRepository,
                                    UserRepository userRepository,
                                    EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.bookingRepository = bookingRepository;
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public List<Payment> getPayments(String email) {
        Vendor vendor = resolveVendor(email);
        return paymentRepository.findByVendor_Id(vendor.getId());
    }

    public List<Invoice> getInvoices(String email) {
        Vendor vendor = resolveVendor(email);
        return invoiceRepository.findByVendor_Id(vendor.getId());
    }

    public Invoice generateInvoice(String email, Long bookingId, Double subtotal, Double taxPercentage, LocalDate dueDate) {
        Vendor vendor = resolveVendor(email);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this booking");
        }

        double taxAmount = subtotal * ((taxPercentage == null ? 0.0 : taxPercentage) / 100.0);
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setBooking(booking);
        invoice.setVendor(vendor);
        invoice.setCustomer(booking.getCustomer());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(dueDate);
        invoice.setSubtotal(subtotal);
        invoice.setTaxPercentage(taxPercentage);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmount(subtotal + taxAmount);
        invoice.setStatus(InvoiceStatus.SENT);
        invoice.setCreatedAt(LocalDateTime.now());
        Invoice saved = invoiceRepository.save(invoice);
        emailService.sendInvoiceEmail(booking.getCustomer().getEmail(), saved.getInvoiceNumber(), "/api/vendor/invoices/" + saved.getId() + "/pdf");
        return saved;
    }

    public Payment recordPayment(String email, Long bookingId, Double amountPaid, PaymentMethod method, String transactionId) {
        Vendor vendor = resolveVendor(email);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!booking.getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this booking");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setVendor(vendor);
        payment.setTotalAmount(booking.getQuotedPrice());
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(method);
        payment.setTransactionId(transactionId);
        payment.setPaymentStatus(amountPaid != null && amountPaid >= booking.getQuotedPrice() ? PaymentStatus.FULLY_PAID : PaymentStatus.PARTIAL_PAID);
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Double getEarningsSummary(String email) {
        Vendor vendor = resolveVendor(email);
        return paymentRepository.findByVendor_Id(vendor.getId()).stream()
                .mapToDouble(payment -> payment.getAmountPaid() == null ? 0.0 : payment.getAmountPaid())
                .sum();
    }

    private Vendor resolveVendor(String email) {
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}
