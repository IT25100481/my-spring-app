package com.example.my_spring_app.dtos;

public class VendorProfileUpdateRequest {
    private String businessName;
    private String businessDescription;
    private String businessLocation;
    private String businessPhone;
    private String website;
    private String category;
    private String serviceAreas;
    private String profilePhotoUrl;
    private String bannerImageUrl;
    private String portfolioMediaUrls;

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessDescription() { return businessDescription; }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }
    public String getBusinessLocation() { return businessLocation; }
    public void setBusinessLocation(String businessLocation) { this.businessLocation = businessLocation; }
    public String getBusinessPhone() { return businessPhone; }
    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }
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
    public String getPortfolioMediaUrls() { return portfolioMediaUrls; }
    public void setPortfolioMediaUrls(String portfolioMediaUrls) { this.portfolioMediaUrls = portfolioMediaUrls; }
}
