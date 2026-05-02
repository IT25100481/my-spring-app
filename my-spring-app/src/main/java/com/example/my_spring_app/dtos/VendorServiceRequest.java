package com.example.my_spring_app.dtos;

public class VendorServiceRequest {
    private String serviceName;
    private String description;
    private Double basePrice;
    private Boolean isAvailable;
    private Integer minimumBookingNoticeDays;
    private String serviceImageUrl;

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getBasePrice() { return basePrice; }
    public void setBasePrice(Double basePrice) { this.basePrice = basePrice; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }
    public Integer getMinimumBookingNoticeDays() { return minimumBookingNoticeDays; }
    public void setMinimumBookingNoticeDays(Integer minimumBookingNoticeDays) { this.minimumBookingNoticeDays = minimumBookingNoticeDays; }
    public String getServiceImageUrl() { return serviceImageUrl; }
    public void setServiceImageUrl(String serviceImageUrl) { this.serviceImageUrl = serviceImageUrl; }
}
