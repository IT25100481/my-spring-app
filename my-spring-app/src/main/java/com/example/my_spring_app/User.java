package com.example.my_spring_app;

import lombok.*;
import java.time.LocalDateTime;

// Removed JPA annotations for text file persistence
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String phone;

    private UserRole role;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private String profileImageUrl;

    public User(String fullName, String email, String password, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = UserRole.CUSTOMER;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
