# Wedding Planner - Vendor Features Analysis

## Executive Summary
The application is in an early-stage development phase with basic vendor registration/authentication implemented. The database layer is using file-based persistence (.txt files) instead of a proper relational database. Most of the 10 core vendor functional requirements remain **NOT IMPLEMENTED**.

---

## 1. IMPLEMENTED VENDOR-RELATED FEATURES

### ✅ Vendor Registration & Authentication (Partial)
**What exists:**
- **Controller**: `RegistrationController.java` - POST endpoint for vendor registration
  - Endpoint: `POST /api/register/vendor`
  - Parameters: businessName, email, password, phone, category
  - Validation: Checks if email already exists (file-based duplicate check)
  - Response: JSON success/error message
- **Service**: `VendorService.java` - File-based persistence
  - Saves vendor to `vendors.txt` in CSV format
  - Email uniqueness validation
  - Retrieves all vendors
- **Model**: `Vendor.java` POJO with properties:
  - businessName, email, password, phone, category
- **Frontend**: `register.html` - has vendor registration form option

**Limitations:**
- ❌ NO persistent password hashing (plain text in files)
- ❌ NO JWT/session token authentication
- ❌ NO login endpoint for vendors (only registration)
- ❌ NO role-based access control
- ❌ NO email verification
- ❌ NO OAuth/social login

---

### ✅ Vendor Dashboard (Partial)
**What exists:**
- **Controller**: `VendorDashboardController.java` - Navigation endpoints
  - GET `/vendor/dashboard` → vendor-dashboard.html
  - GET `/vendor/dashboard/partd` → vendor-dashboard-partd.html
  - GET `/logout` → redirect to login.html
- **Frontend Pages**:
  - `vendor-dashboard.html` - Shows sections for:
    - Active Listings management
    - Inbox/Messages view
    - Performance metrics/reports
    - Quick Actions (Edit Profile, Update Services, Open Partd)
  - `vendor-dashboard-partd.html` - "Partd" section (appears to be a placeholder for service management)
  - `vendor-dashboard-partd.html` - Generic vendor management page

**Limitations:**
- ❌ Dashboard is static HTML with no data binding
- ❌ No actual performance metrics or analytics
- ❌ No functionality behind the buttons (Edit Profile, Update Services, View Messages)
- ❌ No vendor authentication/session check (anyone can access)
- ❌ "Partd" appears to be a placeholder term - unclear purpose

---

### ✅ Basic Frontend Pages
**What exists:**
- `index.html` - Landing page with navigation
- `vendors.html` - Vendor listing page
- `register.html` - Registration form (User & Vendor tabs)
- `login.html` - Login form (static)
- `admin-vendors.html` - Admin vendor management page
- `hotels.html` - Hotels listing
- `gallery.html` - Gallery page

---

## 2. DATABASE MODELS & ARCHITECTURE

### Current Data Models (File-Based, NOT Database)

#### **User Model** (`User.java`)
```java
Fields:
- fullName (String)
- email (String)
- password (String)
- phone (String)

Persistence: users.txt (CSV format)
No relationships, no timestamps, no status tracking
```

#### **Vendor Model** (`Vendor.java`)
```java
Fields:
- businessName (String)
- email (String)
- password (String)
- phone (String)
- category (String)

Persistence: vendors.txt (CSV format)
No relationships, no timestamps, no ratings, no status
```

### ❌ Missing Database Models
- ❌ Service/Package entity (services offered by vendor)
- ❌ Booking entity (customer bookings)
- ❌ Availability/Schedule entity (vendor availability slots)
- ❌ Message/Communication entity (vendor-to-customer messaging)
- ❌ Review/Rating entity (customer reviews)
- ❌ Notification entity (alerts & reminders)
- ❌ Payment/Invoice entity (transactions)
- ❌ Analytics entity (dashboard metrics)

### Current Tech Stack Issues
- **File Storage**: Using plain `.txt` files instead of database
- **No ORM**: No JPA, Hibernate, or any persistence framework
- **No Transactions**: File-based operations have no ACID guarantees
- **No Relationships**: No foreign keys or data integrity
- **No Indexing**: Queries loop through entire files

**pom.xml Dependencies:**
```
- spring-boot-starter-web
- spring-boot-starter-thymeleaf
- lombok
- spring-boot-starter-test
```
❌ Missing: spring-boot-starter-data-jpa, spring-boot-starter-data-rest, database drivers (MySQL, PostgreSQL, H2)

---

## 3. REST API ENDPOINTS

### Currently Implemented
```
POST   /api/register/user         → User registration
POST   /api/register/vendor       → Vendor registration
GET    /api/hello                 → Test endpoint
GET    /vendor/dashboard          → Vendor dashboard (static)
GET    /vendor/dashboard/partd    → Vendor partd section (static)
GET    /logout                    → Logout redirect
```

### ❌ Missing REST Endpoints (All CRUD operations)
- Vendor Profile Management: GET, PUT, DELETE vendors
- Service Management: CRUD operations for services/packages
- Booking Management: Create, read, update, cancel bookings
- Availability: GET/POST/PUT/DELETE availability slots
- Messaging: GET/POST messages, mark read, delete
- Reviews: POST review, GET ratings
- Notifications: GET alerts, mark read
- Payment/Invoicing: GET invoices, process payments
- Analytics: GET dashboard metrics
- Admin: GET all vendors, get all bookings, search/filter

---

## 4. FRONTEND UI COMPONENTS

### Vendor-Related Pages
| Page | Location | Status | Features |
|------|----------|--------|----------|
| Vendor Dashboard | `vendor-dashboard.html` | ✅ Exists | Static nav, 3 summary cards, quick actions |
| Vendor Partd Section | `vendor-dashboard-partd.html` | ✅ Exists | Placeholder for service management |
| Vendor Listing | `vendors.html` | ✅ Exists | Shows vendor listings (static) |
| Registration | `register.html` | ✅ Exists | User & Vendor registration tabs |
| Login | `login.html` | ✅ Exists | Static form, no backend integration |

### UI Framework
- **Bootstrap 5.3** - Responsive design
- **Custom CSS** - Gold/cream/dark theme
- **Google Fonts** - Cormorant Garamond, Montserrat
- **No frontend framework** - Vanilla JavaScript

---

## 5. MAPPING TO 10 VENDOR REQUIREMENTS

### Requirement Status Summary

| # | Requirement | Status | % Complete | Notes |
|---|-------------|--------|-----------|-------|
| 1 | Vendor Registration & Auth | 🟡 Partial | 20% | Registration form & endpoint exist; NO login, passwords not hashed |
| 2 | Vendor Profile Management | ❌ Missing | 0% | Dashboard shows placeholder; no edit/view endpoints |
| 3 | Service & Package Management | ❌ Missing | 0% | No models, endpoints, or UI components |
| 4 | Booking Management | ❌ Missing | 0% | No models, endpoints, or UI components |
| 5 | Availability Management | ❌ Missing | 0% | No models, endpoints, or UI components |
| 6 | Messaging/Communication | ❌ Missing | 0% | Dashboard mentions "Inbox" but no functionality |
| 7 | Reviews & Ratings | ❌ Missing | 0% | No models, endpoints, or UI components |
| 8 | Notifications & Alerts | ❌ Missing | 0% | No models, endpoints, or UI components |
| 9 | Payment & Invoicing | ❌ Missing | 0% | No models, endpoints, or UI components |
| 10 | Dashboard & Analytics | 🟡 Partial | 15% | Static dashboard page exists; no real data/metrics |

**Overall Completion: ~4% of vendor feature requirements**

---

## 6. DETAILED GAP ANALYSIS

### 1️⃣ VENDOR REGISTRATION & AUTHENTICATION
**Status**: 🟡 20% Complete

**Implemented:**
- Registration form with fields: businessName, email, password, phone, category
- POST endpoint for registration
- Email duplication check
- Success/error response messages

**Missing:**
- ❌ Vendor login endpoint
- ❌ Password hashing (uses bcrypt, Spring Security)
- ❌ JWT token generation and validation
- ❌ Session management
- ❌ Email verification flow
- ❌ OAuth/social login
- ❌ Account activation status
- ❌ Password reset/recovery
- ❌ Role-based access control (RBAC)
- ❌ Two-factor authentication

**Database Changes Needed:**
- Add `id`, `createdAt`, `updatedAt` to Vendor
- Add `status` (ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION)
- Add `isEmailVerified` boolean
- Add `passwordHash` instead of plain `password`
- Add `verificationToken` field

---

### 2️⃣ VENDOR PROFILE MANAGEMENT
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Get vendor profile endpoint
- ❌ Update vendor profile endpoint
- ❌ Upload business logo/images
- ❌ Add business description
- ❌ Add business address, hours, certifications
- ❌ Update contact information
- ❌ View/edit vendor stats
- ❌ Profile visibility settings
- ❌ Vendor subscription tier
- ❌ Profile completeness score

**Required Models:**
```java
- VendorProfile (extends Vendor)
  - businessDescription
  - businessAddress
  - businessHours
  - logo (file path)
  - certificates (array)
  - rating
  - totalReviews
  - verificationStatus
```

**Required Endpoints:**
```
GET    /api/vendors/{id}           → Get vendor profile
PUT    /api/vendors/{id}           → Update vendor profile
POST   /api/vendors/{id}/logo      → Upload logo
GET    /api/vendors/{id}/stats     → Get vendor stats
```

---

### 3️⃣ SERVICE & PACKAGE MANAGEMENT
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Service/Package model (name, description, price, duration, etc.)
- ❌ Create service endpoint
- ❌ Update service endpoint
- ❌ Delete service endpoint
- ❌ List services by vendor endpoint
- ❌ Add service images/gallery
- ❌ Set service categories
- ❌ Bulk upload services
- ❌ Service visibility toggle
- ❌ Service pricing tiers

**Required Model:**
```java
public class Service {
  - id (UUID/Long)
  - vendorId
  - name
  - description
  - category (Catering, Photography, Venues, etc.)
  - basePrice
  - duration (in hours)
  - maxGuests
  - images (List<String>)
  - isActive
  - createdAt, updatedAt
}
```

**Required Endpoints:**
```
POST   /api/vendors/{vendorId}/services
GET    /api/vendors/{vendorId}/services
GET    /api/services/{serviceId}
PUT    /api/services/{serviceId}
DELETE /api/services/{serviceId}
POST   /api/services/{serviceId}/images
```

---

### 4️⃣ BOOKING MANAGEMENT
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Booking request model
- ❌ Accept/reject booking endpoint
- ❌ List vendor bookings endpoint
- ❌ View booking details
- ❌ Cancel booking
- ❌ Booking status tracking
- ❌ Booking confirmation/contract
- ❌ Booking timeline/checklist
- ❌ Modification requests for bookings
- ❌ Export booking details

**Required Model:**
```java
public class Booking {
  - id
  - vendorId
  - customerId
  - serviceId
  - eventDate
  - eventLocation
  - guestCount
  - status (PENDING, CONFIRMED, COMPLETED, CANCELLED)
  - totalPrice
  - deposit (%)
  - notes
  - createdAt, updatedAt
}
```

**Required Endpoints:**
```
GET    /api/vendors/{vendorId}/bookings
GET    /api/bookings/{bookingId}
PUT    /api/bookings/{bookingId}/accept
PUT    /api/bookings/{bookingId}/reject
PUT    /api/bookings/{bookingId}/cancel
GET    /api/bookings/{bookingId}/timeline
```

---

### 5️⃣ AVAILABILITY MANAGEMENT
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Availability calendar model
- ❌ Set available dates endpoint
- ❌ Block dates endpoint (vendor unavailable)
- ❌ Get available dates endpoint
- ❌ Time slot management
- ❌ Recurring availability patterns
- ❌ Availability by service type
- ❌ Calendar view
- ❌ Bulk availability upload
- ❌ Travel time between bookings

**Required Model:**
```java
public class Availability {
  - id
  - vendorId
  - date
  - startTime
  - endTime
  - serviceId (optional)
  - isBooked
  - status (AVAILABLE, BLOCKED, BOOKED)
}
```

**Required Endpoints:**
```
POST   /api/vendors/{vendorId}/availability
GET    /api/vendors/{vendorId}/availability?month=2026-05
PUT    /api/availability/{id}
DELETE /api/availability/{id}
GET    /api/vendors/{vendorId}/available-dates
```

---

### 6️⃣ MESSAGING / COMMUNICATION
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Message model
- ❌ Send message endpoint
- ❌ Get conversation list
- ❌ Get conversation thread
- ❌ Mark message as read
- ❌ Delete message
- ❌ Search messages
- ❌ Message notifications (real-time)
- ❌ Attachment support
- ❌ Message templates

**Current Dashboard State:**
- Shows "Inbox" section on vendor dashboard
- No backend integration
- No message count

**Required Model:**
```java
public class Message {
  - id
  - senderId
  - receiverId
  - bookingId (related booking)
  - content
  - attachments (optional)
  - isRead
  - createdAt
}
```

**Required Endpoints:**
```
POST   /api/messages
GET    /api/messages/conversations
GET    /api/messages/conversation/{userId}
PUT    /api/messages/{id}/read
DELETE /api/messages/{id}
GET    /api/messages/search
```

---

### 7️⃣ REVIEWS & RATINGS
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Review/Rating model
- ❌ Submit review endpoint
- ❌ Get vendor reviews endpoint
- ❌ Update review endpoint
- ❌ Delete review endpoint
- ❌ Get vendor average rating
- ❌ Review verification (only verified customers)
- ❌ Vendor response to reviews
- ❌ Review moderation
- ❌ Review photos

**Required Model:**
```java
public class Review {
  - id
  - vendorId
  - customerId
  - bookingId (verified booking)
  - rating (1-5)
  - title
  - comment
  - photos (List<String>)
  - isVerifiedPurchase
  - helpfulCount
  - vendorResponse (optional)
  - createdAt, updatedAt
}
```

**Required Endpoints:**
```
POST   /api/vendors/{vendorId}/reviews
GET    /api/vendors/{vendorId}/reviews
GET    /api/vendors/{vendorId}/rating
PUT    /api/reviews/{id}
DELETE /api/reviews/{id}
POST   /api/reviews/{id}/response
```

---

### 8️⃣ NOTIFICATIONS & ALERTS
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Notification model
- ❌ Create notification endpoint
- ❌ Get vendor notifications
- ❌ Mark notification as read
- ❌ Delete notification
- ❌ Email notifications
- ❌ SMS notifications
- ❌ Push notifications
- ❌ Notification preferences/settings
- ❌ Real-time notification system (WebSocket)

**Notification Types Needed:**
- New booking request
- Booking confirmed/cancelled
- New message received
- New review posted
- Payment received
- Profile view milestone
- Availability expiring
- Invoice due

**Required Model:**
```java
public class Notification {
  - id
  - vendorId
  - type (BOOKING, MESSAGE, REVIEW, PAYMENT, etc.)
  - title
  - message
  - relatedEntityId
  - isRead
  - createdAt
}
```

**Required Endpoints:**
```
GET    /api/vendors/{vendorId}/notifications
PUT    /api/notifications/{id}/read
DELETE /api/notifications/{id}
PUT    /api/vendors/{vendorId}/notification-preferences
```

---

### 9️⃣ PAYMENT & INVOICING
**Status**: ❌ 0% Complete

**Missing:**
- ❌ Invoice model
- ❌ Generate invoice endpoint
- ❌ Get invoice list
- ❌ Download invoice (PDF)
- ❌ Payment tracking
- ❌ Payment integration (Stripe, PayPal)
- ❌ Payment status tracking
- ❌ Refund management
- ❌ Tax calculation
- ❌ Commission tracking (platform fee)

**Required Models:**
```java
public class Invoice {
  - id
  - vendorId
  - bookingId
  - amount
  - tax
  - totalAmount
  - status (DRAFT, SENT, PAID, OVERDUE, CANCELLED)
  - issueDate
  - dueDate
  - paidDate
}

public class Payment {
  - id
  - invoiceId
  - amount
  - method (CARD, BANK_TRANSFER, WALLET)
  - status (PENDING, SUCCESS, FAILED)
  - transactionId (gateway reference)
  - createdAt
}
```

**Required Endpoints:**
```
POST   /api/invoices
GET    /api/vendors/{vendorId}/invoices
GET    /api/invoices/{id}
GET    /api/invoices/{id}/pdf
POST   /api/payments
GET    /api/vendors/{vendorId}/payment-history
GET    /api/vendors/{vendorId}/earnings
PUT    /api/vendors/{vendorId}/payout-settings
```

---

### 🔟 DASHBOARD & ANALYTICS
**Status**: 🟡 15% Complete

**Currently Implemented:**
- Static dashboard HTML page
- Navigation to dashboard
- 3 summary cards (Active Listings, Inbox, Performance) - no data

**Missing:**
- ❌ Dashboard data endpoints
- ❌ Key metrics (total bookings, revenue, ratings)
- ❌ Charts/graphs (booking trends, revenue over time)
- ❌ Recent bookings widget
- ❌ Upcoming events calendar
- ❌ Recent reviews widget
- ❌ Income summary
- ❌ Top services/packages
- ❌ Customer demographics
- ❌ Email/SMS open rates
- ❌ Vendor comparison benchmarks
- ❌ Custom date range filtering
- ❌ Export reports (CSV, PDF)

**Required Model:**
```java
public class VendorDashboardMetrics {
  - vendorId
  - totalBookings
  - completedBookings
  - totalRevenue
  - averageRating
  - profileViews
  - inquiriesReceived
  - responseRate
  - bookingConversionRate
}
```

**Required Endpoints:**
```
GET    /api/vendors/{vendorId}/dashboard
GET    /api/vendors/{vendorId}/metrics
GET    /api/vendors/{vendorId}/metrics/bookings
GET    /api/vendors/{vendorId}/metrics/revenue
GET    /api/vendors/{vendorId}/metrics/engagement
GET    /api/vendors/{vendorId}/recent-bookings
GET    /api/vendors/{vendorId}/recent-reviews
GET    /api/vendors/{vendorId}/analytics?period=month&startDate=2026-01-01&endDate=2026-01-31
```

---

## 7. ARCHITECTURE & INFRASTRUCTURE GAPS

### Database & Persistence
- ❌ No relational database (MySQL, PostgreSQL, etc.)
- ❌ No JPA/Hibernate ORM
- ❌ File-based storage is not scalable
- ❌ No database migrations
- ❌ No data backups
- ❌ No transactions/ACID compliance

### Authentication & Security
- ❌ No Spring Security integration
- ❌ No password hashing (bcrypt, Argon2)
- ❌ No JWT/OAuth2 tokens
- ❌ No CORS configuration
- ❌ No rate limiting
- ❌ No input validation/sanitization
- ❌ No SQL injection protection

### Scalability & Performance
- ❌ No caching layer (Redis)
- ❌ No API pagination
- ❌ No data indexing
- ❌ File I/O bottleneck
- ❌ No load balancing ready
- ❌ No async processing

### Frontend
- ❌ No state management
- ❌ No form validation
- ❌ No API client/service layer
- ❌ No error handling UI
- ❌ No loading states
- ❌ No responsive images
- ❌ No PWA features

### DevOps & Deployment
- ❌ No CI/CD pipeline
- ❌ No environment configuration (dev, staging, prod)
- ❌ No Docker support
- ❌ No logging framework
- ❌ No monitoring/alerts
- ❌ No API documentation (Swagger/OpenAPI)

---

## 8. QUICK START: ROADMAP TO FULL IMPLEMENTATION

### Phase 1: Foundation (Weeks 1-2)
1. Set up proper database (PostgreSQL recommended)
2. Add JPA/Hibernate dependencies
3. Create all required entities with relationships
4. Implement authentication with Spring Security + JWT
5. Set up CORS, validation, error handling

### Phase 2: Core Vendor Features (Weeks 3-5)
1. Implement vendor registration + login
2. Build vendor profile management
3. Create service/package management
4. Implement booking system

### Phase 3: Communication & Reviews (Weeks 6-7)
1. Build messaging system
2. Implement reviews & ratings
3. Set up notifications infrastructure

### Phase 4: Advanced Features (Weeks 8-10)
1. Availability calendar system
2. Payment integration
3. Dashboard & analytics
4. Email notifications

### Phase 5: Polish & Deploy (Weeks 11-12)
1. Frontend improvements
2. Performance optimization
3. Security audit
4. Deployment setup

---

## 9. RECOMMENDED NEXT STEPS

1. **Immediate**: Migrate from file storage to PostgreSQL + JPA
2. **Security**: Implement Spring Security with password hashing and JWT
3. **Priority**: Complete Vendor Profile Management (foundation for other features)
4. **High Value**: Service Management (enables business operations)
5. **High Value**: Booking System (core business process)
6. **Concurrent**: Start Dashboard Analytics foundation

---

## Files to Review
- Controller: [RegistrationController.java](src/main/java/com/example/my_spring_app/RegistrationController.java)
- Controller: [VendorDashboardController.java](src/main/java/com/example/my_spring_app/VendorDashboardController.java)
- Models: [Vendor.java](src/main/java/com/example/my_spring_app/Vendor.java), [User.java](src/main/java/com/example/my_spring_app/User.java)
- Services: [VendorService.java](src/main/java/com/example/my_spring_app/VendorService.java), [UserService.java](src/main/java/com/example/my_spring_app/UserService.java)
- UI: [vendor-dashboard.html](src/main/resources/static/vendor-dashboard.html)
- Config: [pom.xml](pom.xml)
