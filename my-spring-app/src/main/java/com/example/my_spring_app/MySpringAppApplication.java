package com.example.my_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
    basePackages = "com.example.my_spring_app",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
            value = {
                // Exclude problematic controllers
                com.example.my_spring_app.controllers.VendorAnalyticsController.class,
                com.example.my_spring_app.controllers.VendorAvailabilityController.class,
                com.example.my_spring_app.controllers.VendorBookingController.class,
                com.example.my_spring_app.controllers.VendorCatalogController.class,
                com.example.my_spring_app.controllers.VendorMessageController.class,
                com.example.my_spring_app.controllers.VendorNotificationController.class,
                com.example.my_spring_app.controllers.VendorPageController.class,
                com.example.my_spring_app.controllers.VendorPaymentController.class,
                com.example.my_spring_app.controllers.VendorProfileController.class,
                com.example.my_spring_app.controllers.VendorReviewController.class,
                // Exclude problematic services
                com.example.my_spring_app.services.AnalyticsService.class,
                com.example.my_spring_app.services.AvailabilityManagementService.class,
                com.example.my_spring_app.services.BookingManagementService.class,
                com.example.my_spring_app.services.MessageManagementService.class,
                com.example.my_spring_app.services.NotificationManagementService.class,
                com.example.my_spring_app.services.PaymentManagementService.class,
                com.example.my_spring_app.services.ReviewManagementService.class,
                com.example.my_spring_app.services.VendorProfileService.class
            })
    }
)
public class MySpringAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringAppApplication.class, args);
	}

}
