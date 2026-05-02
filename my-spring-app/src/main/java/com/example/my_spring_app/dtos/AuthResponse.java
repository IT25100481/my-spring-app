package com.example.my_spring_app.dtos;

public class AuthResponse {
    private String status;
    private String message;
    private String token;
    private UserInfo user;

    public AuthResponse() {}

    public AuthResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResponse(String status, String message, String token, UserInfo user) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }

    public static class UserInfo {
        private Long id;
        private String email;
        private String fullName;
        private String role;

        public UserInfo() {}

        public UserInfo(Long id, String email, String fullName, String role) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
