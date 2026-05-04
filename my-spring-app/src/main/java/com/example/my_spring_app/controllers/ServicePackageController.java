package com.example.my_spring_app.controllers;

import com.example.my_spring_app.models.ServicePackage;
import com.example.my_spring_app.models.PackageType;
import com.example.my_spring_app.services.ServicePackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class ServicePackageController {

    private final ServicePackageService packageService;

    // ── CREATE ──
    @PostMapping
    public ResponseEntity<ServicePackage> createPackage(@RequestBody ServicePackage servicePackage) {
        ServicePackage created = packageService.addPackage(servicePackage);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── READ ALL ──
    @GetMapping
    public ResponseEntity<List<ServicePackage>> getAllPackages() {
        return ResponseEntity.ok(packageService.getAllPackages());
    }

    // ── READ BY ID ──
    @GetMapping("/{id}")
    public ResponseEntity<ServicePackage> getPackageById(@PathVariable Long id) {
        return packageService.getPackageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── READ BY SERVICE ──
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ServicePackage>> getPackagesByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(packageService.getPackagesByService(serviceId));
    }

    // ── READ BY VENDOR ──
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<ServicePackage>> getPackagesByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(packageService.getPackagesByVendor(vendorId));
    }

    // ── READ BY TYPE ──
    @GetMapping("/type/{packageType}")
    public ResponseEntity<List<ServicePackage>> getPackagesByType(@PathVariable PackageType packageType) {
        return ResponseEntity.ok(packageService.getPackagesByType(packageType));
    }

    // ── UPDATE ──
    @PutMapping("/{id}")
    public ResponseEntity<ServicePackage> updatePackage(@PathVariable Long id,
                                                        @RequestBody ServicePackage servicePackage) {
        try {
            ServicePackage updated = packageService.updatePackage(id, servicePackage);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ── DELETE ──
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    // ── DEACTIVATE (soft delete) ──
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePackage(@PathVariable Long id) {
        packageService.deactivatePackage(id);
        return ResponseEntity.ok().build();
    }
}