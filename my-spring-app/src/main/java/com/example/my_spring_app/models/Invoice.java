package com.example.my_spring_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // This field MUST be named 'amount' for .setAmount() to work in the service
    @Column(nullable = false)
    private Double amount;

    private LocalDate dueDate;

    @Column(name = "issued_date")
    private LocalDate issuedDate = LocalDate.now();

    // You can add a status if you want to track if the invoice is SENT or PAID
    private String status = "ISSUED";
}