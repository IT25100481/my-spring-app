package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    @Autowired
    private com.example.my_spring_app.VendorService coreVendorService;

    public synchronized Vendor saveVendor(Vendor vendor) {
        return coreVendorService.saveVendor(vendor);
    }

    public synchronized Optional<Vendor> findByUserEmail(String email) {
        return coreVendorService.findByUserEmail(email);
    }

    public synchronized Optional<Vendor> findById(Long id) {
        return coreVendorService.findById(id);
    }

    public synchronized boolean emailExists(String email) {
        return coreVendorService.emailExists(email);
    }

    public synchronized List<Vendor> getAllVendors() {
        return coreVendorService.getAllVendors();
    }
}
