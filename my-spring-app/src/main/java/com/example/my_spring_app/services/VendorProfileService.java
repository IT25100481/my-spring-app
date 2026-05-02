package com.example.my_spring_app.services;

import com.example.my_spring_app.Vendor;
import com.example.my_spring_app.dtos.VendorProfileResponse;
import com.example.my_spring_app.dtos.VendorProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorProfileService {

    @Autowired
    private VendorService vendorService;

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    public VendorProfileResponse getVendorProfile(Long vendorId) {
        Vendor vendor = vendorService.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return toResponse(vendor);
    }

    public VendorProfileResponse getVendorProfileByEmail(String email) {
        Vendor vendor = vendorService.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return toResponse(vendor);
    }

    public VendorProfileResponse updateVendorProfile(Long vendorId, VendorProfileUpdateRequest request) {
        Vendor vendor = vendorService.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (request.getBusinessName() != null) vendor.setBusinessName(request.getBusinessName().trim());
        if (request.getBusinessDescription() != null) vendor.setBusinessDescription(request.getBusinessDescription().trim());
        if (request.getBusinessLocation() != null) vendor.setBusinessLocation(request.getBusinessLocation().trim());
        if (request.getBusinessPhone() != null) vendor.setBusinessPhone(request.getBusinessPhone().trim());
        if (request.getWebsite() != null) vendor.setWebsite(request.getWebsite().trim());
        if (request.getCategory() != null) vendor.setCategory(request.getCategory().trim());
        if (request.getServiceAreas() != null) vendor.setServiceAreas(request.getServiceAreas().trim());
        if (request.getProfilePhotoUrl() != null) vendor.setProfilePhotoUrl(request.getProfilePhotoUrl().trim());
        if (request.getBannerImageUrl() != null) vendor.setBannerImageUrl(request.getBannerImageUrl().trim());
        if (request.getPortfolioMediaUrls() != null) vendor.setPortfolioMediaUrls(request.getPortfolioMediaUrls().trim());

        if (vendor.getUser() != null && request.getBusinessName() != null) {
            vendor.getUser().setFullName(request.getBusinessName().trim());
        }
        if (vendor.getUser() != null && request.getBusinessPhone() != null) {
            vendor.getUser().setPhone(request.getBusinessPhone().trim());
        }

        return toResponse(vendorService.saveVendor(vendor));
    }

    public VendorProfileResponse uploadProfilePhoto(Long vendorId, MultipartFile file) {
        String savedPath = saveFile(file, "vendor-profile");
        Vendor vendor = vendorService.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        vendor.setProfilePhotoUrl(savedPath);
        return toResponse(vendorService.saveVendor(vendor));
    }

    public VendorProfileResponse uploadPortfolioMedia(Long vendorId, MultipartFile file) {
        String savedPath = saveFile(file, "vendor-portfolio");
        Vendor vendor = vendorService.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        List<String> portfolioUrls = parseList(vendor.getPortfolioMediaUrls());
        portfolioUrls.add(savedPath);
        vendor.setPortfolioMediaUrls(String.join(",", portfolioUrls));

        return toResponse(vendorService.saveVendor(vendor));
    }

    public List<VendorProfileResponse> searchVendors(String category, String location) {
        return vendorService.getAllVendors().stream()
                .filter(vendor -> category == null || category.isBlank() || category.equalsIgnoreCase(vendor.getCategory()))
                .filter(vendor -> location == null || location.isBlank() || containsIgnoreCase(vendor.getBusinessLocation(), location))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public VendorProfileResponse getPublicVendorProfile(Long vendorId) {
        return getVendorProfile(vendorId);
    }

    private VendorProfileResponse toResponse(Vendor vendor) {
        VendorProfileResponse response = new VendorProfileResponse();
        response.setVendorId(vendor.getId());
        response.setUserId(vendor.getUser() != null ? vendor.getUser().getId() : null);
        response.setBusinessName(vendor.getBusinessName());
        response.setBusinessDescription(vendor.getBusinessDescription());
        response.setBusinessLocation(vendor.getBusinessLocation());
        response.setBusinessPhone(vendor.getBusinessPhone());
        response.setContactEmail(vendor.getUser() != null ? vendor.getUser().getEmail() : null);
        response.setWebsite(vendor.getWebsite());
        response.setCategory(vendor.getCategory());
        response.setServiceAreas(vendor.getServiceAreas());
        response.setProfilePhotoUrl(vendor.getProfilePhotoUrl());
        response.setBannerImageUrl(vendor.getBannerImageUrl());
        response.setPortfolioMediaUrls(parseList(vendor.getPortfolioMediaUrls()));
        response.setVerificationStatus(vendor.getVerificationStatus() != null ? vendor.getVerificationStatus().toString() : null);
        response.setActive(vendor.getIsActive());
        response.setAverageRating(vendor.getAverageRating());
        response.setTotalReviews(vendor.getTotalReviews());
        response.setTotalBookings(vendor.getTotalBookings());
        response.setMonthlyEarnings(vendor.getMonthlyEarnings());
        response.setProfileViews(vendor.getProfileViews());
        return response;
    }

    private String saveFile(MultipartFile file, String prefix) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required");
        }

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
            String safeName = System.currentTimeMillis() + "-" + prefix + "-" + originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path targetLocation = uploadPath.resolve(safeName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + safeName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file: " + ex.getMessage(), ex);
        }
    }

    private List<String> parseList(String raw) {
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String source, String query) {
        return source != null && source.toLowerCase().contains(query.toLowerCase());
    }
}
