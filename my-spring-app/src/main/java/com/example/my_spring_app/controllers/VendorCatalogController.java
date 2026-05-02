package com.example.my_spring_app.controllers;

import com.example.my_spring_app.dtos.PackageRequest;
import com.example.my_spring_app.dtos.VendorServiceRequest;
import com.example.my_spring_app.models.ServicePackage;
import com.example.my_spring_app.models.VendorServiceEntity;
import com.example.my_spring_app.services.VendorCatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins = "*")
public class VendorCatalogController {

    private final VendorCatalogService vendorCatalogService;

    public VendorCatalogController(VendorCatalogService vendorCatalogService) {
        this.vendorCatalogService = vendorCatalogService;
    }

    @GetMapping("/services")
    public ResponseEntity<?> getServices(@RequestParam String email) {
        return ResponseEntity.ok(vendorCatalogService.getVendorServices(email));
    }

    @PostMapping("/services")
    public ResponseEntity<?> createService(@RequestParam String email,
                                           @RequestBody VendorServiceRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(vendorCatalogService.createService(email, request));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @PutMapping("/services/{serviceId}")
    public ResponseEntity<?> updateService(@RequestParam String email,
                                           @PathVariable Long serviceId,
                                           @RequestBody VendorServiceRequest request) {
        try {
            return ResponseEntity.ok(vendorCatalogService.updateService(email, serviceId, request));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<?> deleteService(@RequestParam String email,
                                           @PathVariable Long serviceId) {
        try {
            vendorCatalogService.deleteService(email, serviceId);
            return ResponseEntity.ok(success("Service deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @PatchMapping("/services/{serviceId}/availability")
    public ResponseEntity<?> updateAvailability(@RequestParam String email,
                                                @PathVariable Long serviceId,
                                                @RequestParam Boolean isAvailable) {
        try {
            return ResponseEntity.ok(vendorCatalogService.updateAvailability(email, serviceId, isAvailable));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @GetMapping("/services/{serviceId}/packages")
    public ResponseEntity<List<ServicePackage>> getPackages(@RequestParam String email,
                                                            @PathVariable Long serviceId) {
        return ResponseEntity.ok(vendorCatalogService.getPackagesForService(email, serviceId));
    }

    @GetMapping("/packages")
    public ResponseEntity<List<ServicePackage>> getAllPackages(@RequestParam String email) {
        return ResponseEntity.ok(vendorCatalogService.getPackagesByVendor(email));
    }

    @PostMapping("/services/{serviceId}/packages")
    public ResponseEntity<?> createPackage(@RequestParam String email,
                                           @PathVariable Long serviceId,
                                           @RequestBody PackageRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(vendorCatalogService.createPackage(email, serviceId, request));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @PutMapping("/packages/{packageId}")
    public ResponseEntity<?> updatePackage(@RequestParam String email,
                                           @PathVariable Long packageId,
                                           @RequestBody PackageRequest request) {
        try {
            return ResponseEntity.ok(vendorCatalogService.updatePackage(email, packageId, request));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    @DeleteMapping("/packages/{packageId}")
    public ResponseEntity<?> deletePackage(@RequestParam String email,
                                           @PathVariable Long packageId) {
        try {
            vendorCatalogService.deletePackage(email, packageId);
            return ResponseEntity.ok(success("Package deleted successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(error(ex.getMessage()));
        }
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", message);
        return body;
    }

    private Map<String, String> success(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("status", "success");
        body.put("message", message);
        return body;
    }
}
