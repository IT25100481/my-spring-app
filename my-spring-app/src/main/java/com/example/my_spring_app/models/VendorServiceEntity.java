package com.example.my_spring_app.models;

import com.example.my_spring_app.Vendor;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendor_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private String serviceName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    private Integer minimumBookingNoticeDays = 7;

    private String serviceImageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
