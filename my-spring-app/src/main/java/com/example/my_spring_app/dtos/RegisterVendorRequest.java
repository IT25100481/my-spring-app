package com.example.my_spring_app.dtos;

public class RegisterVendorRequest {
    private String businessName;
    private String email;
    private String password;
    private String phone;
    private String category;

    public RegisterVendorRequest() {}

    public RegisterVendorRequest(String businessName, String email, String password, 
                               String phone, String category) {
        this.businessName = businessName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.category = category;
    }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
