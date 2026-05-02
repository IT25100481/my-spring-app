package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.dtos.PackageRequest;
import com.example.my_spring_app.dtos.VendorServiceRequest;
import com.example.my_spring_app.models.ServicePackage;
import com.example.my_spring_app.models.VendorServiceEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class VendorCatalogService {

    public List<VendorServiceEntity> getVendorServices(String email) {
        return Collections.emptyList();
    }

    public VendorServiceEntity createService(String email, VendorServiceRequest request) {
        throw new UnsupportedOperationException("Vendor catalog operations are not available with text-file persistence yet.");
    }

    public VendorServiceEntity updateService(String email, Long serviceId, VendorServiceRequest request) {
        throw new UnsupportedOperationException("Vendor catalog operations are not available with text-file persistence yet.");
    }

    public void deleteService(String email, Long serviceId) {
        throw new UnsupportedOperationException("Vendor catalog operations are not available with text-file persistence yet.");
    }

    public VendorServiceEntity updateAvailability(String email, Long serviceId, Boolean isAvailable) {
        throw new UnsupportedOperationException("Vendor catalog operations are not available with text-file persistence yet.");
    }

    public List<ServicePackage> getPackagesForService(String email, Long serviceId) {
        return Collections.emptyList();
    }

    public List<ServicePackage> getPackagesByVendor(String email) {
        return Collections.emptyList();
    }

    public ServicePackage createPackage(String email, Long serviceId, PackageRequest request) {
        throw new UnsupportedOperationException("Vendor package operations are not available with text-file persistence yet.");
    }

    public ServicePackage updatePackage(String email, Long packageId, PackageRequest request) {
        throw new UnsupportedOperationException("Vendor package operations are not available with text-file persistence yet.");
    }

    public void deletePackage(String email, Long packageId) {
        throw new UnsupportedOperationException("Vendor package operations are not available with text-file persistence yet.");
    }
}
