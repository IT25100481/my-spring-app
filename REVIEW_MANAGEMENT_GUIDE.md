## Review Management Module - Implementation Summary

### Overview
Complete review management system for the Wedding Planner Spring Boot application with file-based persistence. Customers can submit, edit, and delete reviews; vendors can reply to reviews; admins can manage visibility and flagging.

---

## Files Created/Modified

### 1. **ReviewService.java** (`services/ReviewService.java`)
**Purpose:** Core business logic for review management
- File-based persistence using JSON serialization
- Methods:
  - `submitReview()` - Create new review (one per customer per vendor)
  - `updateReview()` - Edit review (30-day window)
  - `deleteReview()` - Remove review (owner only)
  - `getVendorReviews()` - Public reviews for vendor
  - `getVendorReviewSummary()` - Rating avg + distribution
  - `getMyReviews()` - Customer's reviews
  - `addVendorReply()` - Vendor response
  - `getAllReviewsForVendorAdmin()` - Admin view (all reviews)
  - `toggleVisibility()` - Admin show/hide
  - `flagReview()` / `unflagReview()` - Admin flagging

**Key Features:**
- One review per customer per vendor enforcement
- 30-day edit window
- Rating distribution calculations
- Privacy-safe name masking ("John D.")
- Synchronized file operations for thread safety

---

### 2. **ReviewRequestDTO.java** (`dtos/ReviewRequestDTO.java`)
**Purpose:** Request validation for review submission/update
```java
- vendorId: Long (required)
- rating: Integer (1-5, required)
- reviewText: String (10-2000 chars, required)
```

---

### 3. **ReviewResponseDTO.java** (`dtos/ReviewResponseDTO.java`)
**Purpose:** Response object with all review details
```java
- id, vendorId, vendorName
- customerId, customerName (masked)
- rating, reviewText
- vendorResponse, vendorResponseAt
- isApproved, isFlagged, flagReason
- createdAt, updatedAt
```

---

### 4. **ReviewController.java** (`controllers/ReviewController.java`)
**Purpose:** REST API endpoints for all review operations

#### **Public Endpoints (No Auth)**
- `GET /api/reviews/vendor/{vendorId}` - List approved reviews
- `GET /api/reviews/vendor/{vendorId}/summary` - Rating summary

#### **Customer Endpoints (JWT Required)**
- `POST /api/reviews` - Submit review
- `PUT /api/reviews/{reviewId}` - Edit own review
- `DELETE /api/reviews/{reviewId}` - Delete own review
- `GET /api/reviews/my-reviews` - View own reviews

#### **Vendor Endpoints (JWT Required)**
- `POST /api/reviews/{reviewId}/reply` - Reply to review

#### **Admin Endpoints (JWT Required)**
- `GET /api/reviews/admin/vendor/{vendorId}` - All reviews (incl. flagged)
- `PATCH /api/reviews/admin/{reviewId}/toggle` - Show/hide review
- `PATCH /api/reviews/admin/{reviewId}/flag` - Flag inappropriate
- `PATCH /api/reviews/admin/{reviewId}/unflag` - Remove flag

---

### 5. **Review.java Model** (`models/Review.java`)
**Purpose:** Review entity (updated for file persistence)

**Fields:**
```java
- id: Long
- vendor: Vendor
- customer: User
- rating: Integer (1-5)
- ratingScore: Float
- reviewText: String (up to 2000 chars)
- vendorResponse: String (vendor reply)
- isApproved: Boolean (visibility control)
- isFlagged: Boolean (inappropriate flag)
- flagReason: String
- createdAt, vendorResponseAt, updatedAt: LocalDateTime
```

---

### 6. **reviews.html** (`static/reviews.html`)
**Purpose:** Interactive frontend for review management

**Features:**
- **Summary Card:** Average rating, star display, distribution bar chart
- **All Reviews Tab:** Public reviews with vendor responses
- **My Reviews Tab:** Customer's own reviews with edit/delete buttons
- **Submit Review Tab:** Interactive star rating, textarea with char count
- **Authentication:** Login redirect for non-logged-in users
- **Responsive Design:** Mobile-friendly grid layout
- **Real-time Feedback:** Toast notifications for user actions

**UI Components:**
- 5-star interactive rating selector
- Character counter (10-2000 limit)
- Review cards with timestamps
- Vendor reply display with date
- Empty states with helpful messages
- Edit/Delete buttons (owner only)
- Rating distribution visualization

---

### 7. **reviews.txt** (Data File)
Storage file for review data (JSON lines format). Automatically created on first review submission.

---

## Data Flow

### **Submitting a Review**
1. Customer clicks "Submit Review" tab
2. Fills form: rating + text
3. POST `/api/reviews` with JWT token
4. Service validates: vendor exists, no duplicate review, rating 1-5, text length
5. Creates Review object with ID, timestamps, approval status
6. Saves to reviews.txt
7. Returns ReviewResponseDTO

### **Reading Reviews**
1. Public GET `/api/reviews/vendor/{vendorId}`
2. Service reads reviews.txt, filters by vendor, approved=true, flagged=false
3. Masks customer names
4. Returns sorted by date descending

### **Vendor Response**
1. Vendor sees review and clicks reply
2. POST `/api/reviews/{reviewId}/reply` with JWT
3. Service verifies vendor owns the review's vendor
4. Adds vendorResponse + vendorResponseAt timestamp
5. Saves back to reviews.txt

### **Admin Management**
1. Admin views `/api/reviews/admin/vendor/{vendorId}`
2. Gets ALL reviews (even flagged/hidden)
3. Can PATCH to toggle approval or flag as inappropriate
4. Changes saved back to file

---

## Integration Points

### JWT Authentication
- Uses existing `JwtTokenProvider.getUserIdFromToken(token)`
- Extracts from "Authorization: Bearer {token}" header
- Validates user exists before operations

### User/Vendor Services
- `UserService.findById()` - Verify customer
- `VendorService.findById()` - Verify vendor
- Customer and vendor data embedded in Review object

### File Persistence Pattern
- Follows UserService convention:
  - Read entire file → List<Review>
  - Modify list in memory
  - Write entire list back to file
  - Synchronized methods for thread safety

---

## Testing Quick Start

### 1. **Submit a Review** (as customer with JWT)
```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "vendorId": 1,
    "rating": 5,
    "reviewText": "Absolutely amazing service! Highly recommended for weddings."
  }'
```

### 2. **View Reviews** (public)
```bash
curl http://localhost:8080/api/reviews/vendor/1
```

### 3. **View Summary** (public)
```bash
curl http://localhost:8080/api/reviews/vendor/1/summary
```

### 4. **View My Reviews** (customer)
```bash
curl http://localhost:8080/api/reviews/my-reviews \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. **Access Frontend**
Navigate to: `http://localhost:8080/reviews.html?vendorId=1`

---

## Validation Rules

| Rule | Validation |
|------|-----------|
| Rating | 1-5 only |
| Review Length | 10-2000 characters |
| Customer | Must be authenticated |
| Duplicate | One review per customer per vendor |
| Edit Window | 30 days from creation |
| Vendor Reply | Vendor must own the vendor |
| Admin Actions | All reviews accessible |

---

## Error Handling

Examples:
- "Customer not found" - Invalid user attempting review
- "Vendor not found" - Invalid vendor ID
- "You have already reviewed this vendor" - Duplicate prevention
- "Rating must be between 1 and 5" - Invalid rating
- "Review must be edited within 30 days of creation" - Edit window expired
- "You can only edit your own reviews" - Ownership check

---

## File Storage Format

**reviews.txt** (JSON Lines, one review per line):
```json
{"id":1,"customer":{"id":1,"fullName":"Jane Doe",...},"vendor":{"id":1,"businessName":"Dream Weddings",...},"rating":5,"reviewText":"Excellent service!","isApproved":true,"isFlagged":false,...}
```

---

## Next Steps

1. **Test review submission** via API or frontend
2. **Link reviews.html** from vendor detail pages: `<a href="reviews.html?vendorId={{vendor.id}}">View Reviews</a>`
3. **Add review count** to vendor listings
4. **Create admin dashboard** for review moderation
5. **Add review notifications** email to vendors
6. **Implement review moderation** workflow

---

## Security Notes

- ✅ JWT authentication required for submissions
- ✅ Customer names masked for privacy
- ✅ Ownership checks prevent unauthorized edits/deletes
- ✅ Admin-only visibility toggles
- ✅ Review flagging for inappropriate content
- ✅ Synchronized file operations prevent race conditions

