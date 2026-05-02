package com.example.my_spring_app.dtos;

import java.util.List;

public class VendorProfileResponse {
    private Long vendorId;
    private Long userId;
    private String businessName;
    private String businessDescription;
    private String businessLocation;
    private String businessPhone;
    private String contactEmail;
    private String website;
    private String category;
    private String serviceAreas;
    private String profilePhotoUrl;
    private String bannerImageUrl;
    private List<String> portfolioMediaUrls;
    private String verificationStatus;
    private Boolean active;
    private Double averageRating;
    private Integer totalReviews;
    private Integer totalBookings;
    private Double monthlyEarnings;
    private Integer profileViews;

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessDescription() { return businessDescription; }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }
    public String getBusinessLocation() { return businessLocation; }
    public void setBusinessLocation(String businessLocation) { this.businessLocation = businessLocation; }
    public String getBusinessPhone() { return businessPhone; }
    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getServiceAreas() { return serviceAreas; }
    public void setServiceAreas(String serviceAreas) { this.serviceAreas = serviceAreas; }
    public String getProfilePhotoUrl() { return profilePhotoUrl; }
    public void setProfilePhotoUrl(String profilePhotoUrl) { this.profilePhotoUrl = profilePhotoUrl; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }
    public List<String> getPortfolioMediaUrls() { return portfolioMediaUrls; }
    public void setPortfolioMediaUrls(List<String> portfolioMediaUrls) { this.portfolioMediaUrls = portfolioMediaUrls; }
    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public Integer getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Integer totalBookings) { this.totalBookings = totalBookings; }
    public Double getMonthlyEarnings() { return monthlyEarnings; }
    public void setMonthlyEarnings(Double monthlyEarnings) { this.monthlyEarnings = monthlyEarnings; }
    public Integer getProfileViews() { return profileViews; }
    public void setProfileViews(Integer profileViews) { this.profileViews = profileViews; }
}
