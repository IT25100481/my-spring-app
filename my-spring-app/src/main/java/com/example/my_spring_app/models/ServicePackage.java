package com.example.my_spring_app.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private VendorServiceEntity service;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Column(nullable = false)
    private String packageName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(length = 2000)
    private String inclusions;

    @Column(length = 1000)
    private String exclusions;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
