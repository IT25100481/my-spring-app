package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.dtos.PackageRequest;
import com.example.my_spring_app.dtos.VendorServiceRequest;
import com.example.my_spring_app.models.PackageType;
import com.example.my_spring_app.models.ServicePackage;
import com.example.my_spring_app.models.VendorServiceEntity;
import com.example.my_spring_app.repositories.ServicePackageRepository;
import com.example.my_spring_app.repositories.VendorRepository;
import com.example.my_spring_app.repositories.VendorServiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorCatalogService {

    private final VendorRepository vendorRepository;
    private final VendorServiceRepository vendorServiceRepository;
    private final ServicePackageRepository servicePackageRepository;

    public VendorCatalogService(VendorRepository vendorRepository,
                                VendorServiceRepository vendorServiceRepository,
                                ServicePackageRepository servicePackageRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorServiceRepository = vendorServiceRepository;
        this.servicePackageRepository = servicePackageRepository;
    }

    public List<VendorServiceEntity> getVendorServices(String email) {
        Vendor vendor = resolveVendor(email);
        return vendorServiceRepository.findByVendor_Id(vendor.getId());
    }

    public VendorServiceEntity createService(String email, VendorServiceRequest request) {
        Vendor vendor = resolveVendor(email);
        VendorServiceEntity service = new VendorServiceEntity();
        service.setVendor(vendor);
        applyRequest(service, request);
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());
        return vendorServiceRepository.save(service);
    }

    public VendorServiceEntity updateService(String email, Long serviceId, VendorServiceRequest request) {
        VendorServiceEntity service = getOwnedService(email, serviceId);
        applyRequest(service, request);
        service.setUpdatedAt(LocalDateTime.now());
        return vendorServiceRepository.save(service);
    }

    public void deleteService(String email, Long serviceId) {
        VendorServiceEntity service = getOwnedService(email, serviceId);
        servicePackageRepository.findByService_Id(service.getId()).forEach(servicePackageRepository::delete);
        vendorServiceRepository.delete(service);
    }

    public VendorServiceEntity updateAvailability(String email, Long serviceId, Boolean isAvailable) {
        VendorServiceEntity service = getOwnedService(email, serviceId);
        service.setIsAvailable(isAvailable != null ? isAvailable : Boolean.TRUE);
        service.setUpdatedAt(LocalDateTime.now());
        return vendorServiceRepository.save(service);
    }

    public List<ServicePackage> getPackagesForService(String email, Long serviceId) {
        VendorServiceEntity service = getOwnedService(email, serviceId);
        return servicePackageRepository.findByService_Id(service.getId());
    }

    public List<ServicePackage> getPackagesByVendor(String email) {
        Vendor vendor = resolveVendor(email);
        return servicePackageRepository.findByService_Vendor_Id(vendor.getId());
    }

    public ServicePackage createPackage(String email, Long serviceId, PackageRequest request) {
        VendorServiceEntity service = getOwnedService(email, serviceId);
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setService(service);
        applyPackageRequest(servicePackage, request);
        servicePackage.setCreatedAt(LocalDateTime.now());
        servicePackage.setUpdatedAt(LocalDateTime.now());
        return servicePackageRepository.save(servicePackage);
    }

    public ServicePackage updatePackage(String email, Long packageId, PackageRequest request) {
        ServicePackage servicePackage = getOwnedPackage(email, packageId);
        applyPackageRequest(servicePackage, request);
        servicePackage.setUpdatedAt(LocalDateTime.now());
        return servicePackageRepository.save(servicePackage);
    }

    public void deletePackage(String email, Long packageId) {
        ServicePackage servicePackage = getOwnedPackage(email, packageId);
        servicePackageRepository.delete(servicePackage);
    }

    private void applyRequest(VendorServiceEntity service, VendorServiceRequest request) {
        if (request.getServiceName() != null) service.setServiceName(request.getServiceName().trim());
        if (request.getDescription() != null) service.setDescription(request.getDescription().trim());
        if (request.getBasePrice() != null) service.setBasePrice(request.getBasePrice());
        if (request.getIsAvailable() != null) service.setIsAvailable(request.getIsAvailable());
        if (request.getMinimumBookingNoticeDays() != null) service.setMinimumBookingNoticeDays(request.getMinimumBookingNoticeDays());
        if (request.getServiceImageUrl() != null) service.setServiceImageUrl(request.getServiceImageUrl().trim());
    }

    private void applyPackageRequest(ServicePackage servicePackage, PackageRequest request) {
        if (request.getPackageType() != null) {
            servicePackage.setPackageType(PackageType.valueOf(request.getPackageType().trim().toUpperCase()));
        }
        if (request.getPackageName() != null) servicePackage.setPackageName(request.getPackageName().trim());
        if (request.getDescription() != null) servicePackage.setDescription(request.getDescription().trim());
        if (request.getPrice() != null) servicePackage.setPrice(request.getPrice());
        if (request.getInclusions() != null) servicePackage.setInclusions(request.getInclusions().trim());
        if (request.getExclusions() != null) servicePackage.setExclusions(request.getExclusions().trim());
        if (request.getIsActive() != null) servicePackage.setIsActive(request.getIsActive());
    }

    private Vendor resolveVendor(String email) {
        return vendorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }

    private VendorServiceEntity getOwnedService(String email, Long serviceId) {
        Vendor vendor = resolveVendor(email);
        VendorServiceEntity service = vendorServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        if (!service.getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this service");
        }
        return service;
    }

    private ServicePackage getOwnedPackage(String email, Long packageId) {
        Vendor vendor = resolveVendor(email);
        ServicePackage servicePackage = servicePackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        if (!servicePackage.getService().getVendor().getId().equals(vendor.getId())) {
            throw new RuntimeException("You do not own this package");
        }
        return servicePackage;
    }
}
