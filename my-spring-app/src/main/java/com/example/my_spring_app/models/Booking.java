package com.example.my_spring_app.models;

import com.example.my_spring_app.User;
import com.example.my_spring_app.Vendor;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private VendorServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(length = 500)
    private String eventLocation;

    @Column(length = 1000)
    private String eventDetails;

    @Column(length = 1000)
    private String coupleInfo;

    @Column(nullable = false)
    private Double quotedPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.INQUIRY;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private LocalDate confirmedDate;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
