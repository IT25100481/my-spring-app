package com.example.my_spring_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendorDashboardController {

    @GetMapping("/vendor/dashboard")
    public String vendorDashboard() {
        return "forward:/vendor-dashboard.html";
    }

    @GetMapping("/vendor/dashboard/partd")
    public String vendorDashboardPartd() {
        return "forward:/vendor-dashboard-partd.html";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login.html";
    }
}
