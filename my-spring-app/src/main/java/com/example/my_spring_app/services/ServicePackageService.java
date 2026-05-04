package com.example.my_spring_app.services;

import com.example.my_spring_app.models.ServicePackage;
import com.example.my_spring_app.models.PackageType;
import com.example.my_spring_app.repositories.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicePackageService {

    private final ServicePackageRepository packageRepository;

    // ── CREATE ──
    public ServicePackage addPackage(ServicePackage servicePackage) {
        return packageRepository.save(servicePackage);
    }

    // ── READ ──
    public List<ServicePackage> getAllPackages() {
        return packageRepository.findAll();
    }

    public List<ServicePackage> getPackagesByService(Long serviceId) {
        return packageRepository.findByService_Id(serviceId);
    }

    public List<ServicePackage> getPackagesByVendor(Long vendorId) {
        return packageRepository.findByService_Vendor_Id(vendorId);
    }

    public List<ServicePackage> getPackagesByType(PackageType packageType) {
        return packageRepository.findByPackageType(packageType);
    }

    public Optional<ServicePackage> getPackageById(Long id) {
        return packageRepository.findById(id);
    }

    // ── UPDATE ──
    public ServicePackage updatePackage(Long id, ServicePackage updatedPackage) {
        return packageRepository.findById(id).map(existing -> {
            existing.setPackageName(updatedPackage.getPackageName());
            existing.setPackageType(updatedPackage.getPackageType());
            existing.setDescription(updatedPackage.getDescription());
            existing.setPrice(updatedPackage.getPrice());
            existing.setInclusions(updatedPackage.getInclusions());
            existing.setExclusions(updatedPackage.getExclusions());
            existing.setIsActive(updatedPackage.getIsActive());
            return packageRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
    }

    // ── DELETE ──
    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }

    public void deactivatePackage(Long id) {
        packageRepository.findById(id).ifPresent(pkg -> {
            pkg.setIsActive(false);
            packageRepository.save(pkg);
        });
    }
}