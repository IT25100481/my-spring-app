# Vendor Management System - Implementation Guide

## Overview
This document provides a complete implementation roadmap for all 10 vendor functional requirements for the Wedding Planning Site.

## Current Status
✅ Database entities created with JPA
✅ Repositories configured
✅ Spring Security foundation set up with JWT
⏳ In Progress: Authentication & Controllers

## Architecture Overview
```
Frontend (HTML/JS) → REST API (Spring Controllers)
                ↓
         Business Logic (Services)
                ↓
         Data Access (Repositories)
                ↓
         Database (PostgreSQL)
```

## Implementation Phases

### Phase 1: Authentication & User Management ✅ (Partially Complete)
**Status**: Setting up, ~30% complete

**Files Created:**
- `JwtTokenProvider.java` - JWT token generation and validation
- `JwtAuthenticationFilter.java` - JWT filtering
- `SecurityConfig.java` - Spring Security configuration
- `CustomUserDetailsService.java` - User details loading from database

**Still Needed:**
1. Update `User` class to add UserRole enum and authentication methods
2. Create `AuthService.java` - Business logic for authentication
3. Create `AuthController.java` - REST endpoints for:
   - User Registration (POST /api/auth/register)
   - User Login (POST /api/auth/login)
   - Vendor Registration (POST /api/auth/register-vendor)
   - Vendor Login (POST /api/auth/login)
   - Password Reset (POST /api/auth/password-reset)
   - Refresh Token (POST /api/auth/refresh)
4. Create DTOs for request/response objects:
   - `RegisterRequest.java`
   - `LoginRequest.java`
   - `AuthResponse.java`

### Phase 2: Vendor Profile Management (0% complete)
**Functional Requirements:**
- Create and edit business profile
- Upload profile/portfolio images
- Set service areas
- Display ratings and reviews

**Required Components:**
1. `VendorService.java` - Business logic for vendor operations
2. `VendorController.java` - REST endpoints:
   - GET /api/vendor/profile - Get vendor profile
   - PUT /api/vendor/profile - Update vendor profile
   - POST /api/vendor/profile/upload-image - Upload images
   - GET /api/vendor/{id}/details - Get public vendor details
   - GET /api/vendor/search - Search vendors by category/location
3. Create `ImageUploadService.java` - Image upload handling
4. Create DTOs:
   - `VendorProfileDTO.java`
   - `VendorUpdateRequest.java`

**Database Models:** ✅ Already created as `Vendor` entity

### Phase 3: Service & Package Management (0% complete)
**Functional Requirements:**
- Add/Edit/Delete services
- Create pricing packages (Basic, Premium, Custom)
- Set package inclusions and descriptions
- Mark services as available/unavailable
- Set minimum booking notice

**Required Components:**
1. `ServiceManagementService.java` - Business logic
2. `ServiceController.java` - REST endpoints:
   - POST /api/vendor/services - Create service
   - PUT /api/vendor/services/{id} - Update service
   - DELETE /api/vendor/services/{id} - Delete service
   - GET /api/vendor/services - List vendor services
   - GET /api/services/{id}/packages - Get packages for service
   - POST /api/services/{id}/packages - Create package
   - PUT /api/packages/{id} - Update package
   - DELETE /api/packages/{id} - Delete package
3. DTOs:
   - `ServiceCreateRequest.java`
   - `PackageCreateRequest.java`

**Database Models:** ✅ Already created as:
- `VendorServiceEntity.java`
- `ServicePackage.java`

### Phase 4: Booking Management (0% complete)
**Functional Requirements:**
- View incoming booking requests
- Accept/Decline/Negotiate bookings
- View booking details
- Mark as Confirmed/Completed/Cancelled
- Calendar view of bookings
- Prevent double-booking

**Required Components:**
1. `BookingService.java` - Business logic for bookings
2. `BookingController.java` - REST endpoints:
   - GET /api/vendor/bookings - List all bookings for vendor
   - GET /api/vendor/bookings/{id} - Get booking details
   - POST /api/vendor/bookings/{id}/accept - Accept booking
   - POST /api/vendor/bookings/{id}/decline - Decline booking
   - POST /api/vendor/bookings/{id}/confirm - Confirm booking
   - PUT /api/vendor/bookings/{id} - Update booking/quote
   - POST /api/vendor/bookings/{id}/complete - Mark as completed
   - POST /api/vendor/bookings/{id}/cancel - Cancel booking
   - GET /api/vendor/bookings/calendar - Calendar view
   - POST /api/customer/bookings - Create booking request
3. `BookingValidator.java` - Prevent double-booking logic
4. DTOs:
   - `BookingRequestCreateDTO.java`
   - `BookingUpdateDTO.java`
   - `BookingResponseDTO.java`

**Database Models:** ✅ Already created as `Booking.java`

### Phase 5: Availability Management (0% complete)
**Functional Requirements:**
- Set available/blocked dates on calendar
- Set working hours/days
- Auto-block dates when booking confirmed
- Calendar-based UI

**Required Components:**
1. `AvailabilityService.java` - Business logic
2. `AvailabilityController.java` - REST endpoints:
   - GET /api/vendor/availability - Get availability calendar
   - POST /api/vendor/availability - Set availability
   - PUT /api/vendor/availability/{id} - Update
   - DELETE /api/vendor/availability/{id} - Remove
   - POST /api/vendor/availability/block-dates - Block dates
3. Create `AvailabilityCalendarDTO.java`
4. Frontend: `vendor-availability.html` with calendar widget

**Database Models:** ✅ Already created as `Availability.java`

### Phase 6: Messaging System (0% complete)
**Functional Requirements:**
- In-app chat with customers
- Receive/reply to enquiries
- Notifications for new messages
- Attach files/quotes

**Required Components:**
1. `MessageService.java` - Business logic
2. `MessageController.java` - REST endpoints:
   - GET /api/messages - Get conversation threads
   - GET /api/messages/{userId} - Get messages with user
   - POST /api/messages - Send message
   - PUT /api/messages/{id}/read - Mark as read
   - POST /api/messages/{id}/attachments - Attach files
3. WebSocket support for real-time messaging
4. DTOs:
   - `MessageCreateRequest.java`
   - `MessageDTO.java`

**Database Models:** ✅ Already created as `Message.java`

### Phase 7: Reviews & Ratings (0% complete)
**Functional Requirements:**
- View reviews left by customers
- Respond to reviews publicly
- Flag inappropriate reviews

**Required Components:**
1. `ReviewService.java` - Business logic
2. `ReviewController.java` - REST endpoints:
   - GET /api/vendor/reviews - Get all reviews
   - POST /api/vendor/reviews/{id}/respond - Respond to review
   - POST /api/vendor/reviews/{id}/flag - Flag review
   - DELETE /api/vendor/reviews/{id} - Delete own review (customers)
   - POST /api/vendor/reviews - Create review (customers)
3. Calculate and update vendor average rating
4. DTOs:
   - `ReviewCreateRequest.java`
   - `ReviewResponseDTO.java`

**Database Models:** ✅ Already created as `Review.java`

### Phase 8: Notifications & Alerts (0% complete)
**Functional Requirements:**
- New booking request notification
- Payment received notification
- New message alert
- Review posted alert
- Booking reminder

**Required Components:**
1. `NotificationService.java` - Business logic
2. `NotificationController.java` - REST endpoints:
   - GET /api/notifications - Get all notifications
   - GET /api/notifications/unread - Get unread count
   - PUT /api/notifications/{id}/read - Mark as read
   - DELETE /api/notifications/{id} - Delete
3. `EmailService.java` - Send email notifications
4. `ScheduledNotificationTask.java` - Schedule reminders

**Database Models:** ✅ Already created as `Notification.java`

### Phase 9: Payment & Invoicing (0% complete)
**Functional Requirements:**
- View payment status per booking
- Generate and send invoices
- Set deposit requirements
- View earnings history
- Payment gateway integration

**Required Components:**
1. `PaymentService.java` - Business logic
2. `PaymentController.java` - REST endpoints:
   - GET /api/vendor/payments - Payment history
   - POST /api/vendor/payments/initiate - Initiate payment
   - POST /api/vendor/payments/webhook - Payment gateway webhook
3. `InvoiceService.java` - Invoice generation
4. `InvoiceController.java` - REST endpoints:
   - POST /api/vendor/invoices - Generate invoice
   - GET /api/vendor/invoices/{id} - Get invoice
   - POST /api/vendor/invoices/{id}/send - Send invoice
   - GET /api/vendor/invoices/{id}/pdf - Download as PDF
5. `PaymentGatewayIntegration.java` - PayHere integration
6. DTOs:
   - `PaymentDTO.java`
   - `InvoiceDTO.java`

**Database Models:** ✅ Already created as:
- `Payment.java`
- `Invoice.java`

### Phase 10: Dashboard & Analytics (0% complete)
**Functional Requirements:**
- Overview: bookings, pending, earnings, profile views, ratings
- Booking trends chart
- Most popular services
- Revenue analytics

**Required Components:**
1. `DashboardService.java` - Calculate KPIs
2. `AnalyticsController.java` - REST endpoints:
   - GET /api/vendor/dashboard - Dashboard data
   - GET /api/vendor/analytics/bookings - Booking trends
   - GET /api/vendor/analytics/revenue - Revenue analytics
   - GET /api/vendor/analytics/services - Service performance
3. Create `DashboardDTO.java` with all metrics
4. Frontend: `vendor-dashboard.html` with charts (Chart.js)

## Frontend Requirements

### HTML Pages to Create/Update:
1. ✅ `register.html` - User/Vendor registration
2. ✅ `login.html` - Login page
3. ✅ `vendors.html` - Browse vendors
4. `vendor-dashboard.html` - Main dashboard
5. `vendor-profile.html` - Profile management
6. `vendor-services.html` - Service management
7. `vendor-bookings.html` - Booking management
8. `vendor-availability.html` - Availability calendar
9. `vendor-messages.html` - Messaging
10. `vendor-reviews.html` - Reviews
11. `vendor-payments.html` - Payment/invoicing
12. `vendor-analytics.html` - Analytics dashboard

### JavaScript Utilities:
1. `auth.js` - Auth token management
2. `api-client.js` - REST API calls
3. `calendar.js` - Calendar widget
4. `charts.js` - Chart rendering

## Configuration Requirements

### application.properties
```properties
# Database (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/wedding_planner
spring.datasource.username=postgres
spring.datasource.password=your_password

# JWT
jwt.secret=your_super_secret_key_that_should_be_at_least_256_bits_long
jwt.expiration=86400000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password

# File Upload
file.upload-dir=uploads/
max.file.size=10485760
```

## Next Immediate Steps

1. **Create AuthService & AuthController** - Enable user/vendor login
2. **Create VendorService & VendorController** - Profile management
3. **Create ServiceManagementService** - Service/package CRUD
4. **Create BookingService** - Booking request handling
5. **Test database connection** - Verify PostgreSQL setup

## Database Setup

```sql
-- PostgreSQL setup
CREATE DATABASE wedding_planner;

-- Connect to database and create schema
\c wedding_planner

-- Hibernate/JPA will auto-create tables based on entities
-- Just run the application with spring.jpa.hibernate.ddl-auto=create
```

## Testing

For each phase, test:
1. ✅ Entity creation and relationships
2. ✅ Repository queries
3. API endpoints with Postman
4. Frontend integration
5. Error handling and validation

## Error Handling

Create `GlobalExceptionHandler.java`:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle all exceptions with proper HTTP status codes
}
```

## Security Notes
- All passwords stored as BCrypt hash ✅
- JWT tokens used for stateless authentication ✅
- Role-based access control (RBAC) configured ✅
- CORS enabled for frontend
- CSRF protection disabled for API (stateless)

## Performance Considerations
- Database indexing on frequently queried fields
- Pagination for large result sets
- Caching for vendor categories and services
- Image optimization and resizing

---

**Total Estimated Implementation Time**: 60-80 hours for complete development + testing

**Current Progress**: ~15% (Foundation setup)
