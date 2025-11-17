# 🔐 OAuth2 JWT Security Implementation Guide

## Overview

Your Laptop Store API now includes OAuth2 security with JWT tokens. Users must register, login, and use a JWT token to access the API.

---

## 🚀 Quick Start

### 1. Register a New User

```bash
curl -X POST http://localhost:8085/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "enabled": true,
  "createdAt": "2025-11-16T17:50:00"
}
```

---

### 2. Login and Get JWT Token

```bash
curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "username": "john_doe",
  "email": "john@example.com"
}
```

---

### 3. Use Token to Access Protected Endpoints

```bash
curl -X GET http://localhost:8085/api/laptops \
  -H "Authorization: Bearer <your_jwt_token>"
```

Replace `<your_jwt_token>` with the actual token from login response.

---

## 📋 Authentication Endpoints

### Sign Up
```bash
POST /api/auth/signup
Content-Type: application/json

{
  "username": "user123",
  "email": "user@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Response:** 201 Created
```json
{
  "id": 1,
  "username": "user123",
  "email": "user@example.com",
  "enabled": true,
  "createdAt": "2025-11-16T17:50:00"
}
```

---

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "user123",
  "password": "password123"
}
```

**Response:** 200 OK
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000,
  "username": "user123",
  "email": "user@example.com"
}
```

---

### Get User Info
```bash
GET /api/auth/user/{username}
Authorization: Bearer <token>
```

**Response:** 200 OK
```json
{
  "id": 1,
  "username": "user123",
  "email": "user@example.com",
  "enabled": true,
  "createdAt": "2025-11-16T17:50:00"
}
```

---

### Validate Token
```bash
POST /api/auth/validate
Authorization: Bearer <token>
```

**Response:** 200 OK
```
true (or false)
```

---

### Auth Health Check
```bash
GET /api/auth/health
```

**Response:** 200 OK
```
"Auth API is running"
```

---

## 🔒 Using JWT Token with API

### All Protected Endpoints Require Token

Every request to `/api/laptops/**` endpoints (except health) requires a valid JWT token in the Authorization header:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWI...
```

### Example: Create Laptop with Token

```bash
curl -X POST http://localhost:8085/api/laptops \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_token>" \
  -d '{
    "brand": "Dell",
    "model": "XPS 13",
    "processor": "Intel i7",
    "ram": "16GB",
    "storage": "512GB SSD",
    "graphicsCard": "Intel Iris Xe",
    "price": 1299.99,
    "stock": 10,
    "description": "Premium ultrabook"
  }'
```

---

## 🔑 JWT Token Details

- **Type**: HS256 (HMAC with SHA-256)
- **Expiration**: 24 hours (86400000 ms)
- **Secret Key**: Configured in `application.properties`
- **Claims**: 
  - `sub` (subject): username
  - `email`: user's email
  - `iat` (issued at): creation time
  - `exp` (expiration): expiration time

---

## ⚙️ Security Configuration

### SecurityConfig.java

**Public Endpoints** (no token required):
- `POST /api/auth/**` (signup, login)
- `GET /api/laptops/health`
- `GET /api/auth/health`

**Protected Endpoints** (token required):
- `GET /api/laptops/**`
- `POST /api/laptops/**`
- `PUT /api/laptops/**`
- `PATCH /api/laptops/**`
- `DELETE /api/laptops/**`

---

## 🔐 Password Security

- Passwords are **hashed** using BCrypt
- Never stored in plain text
- Minimum 6 characters required
- Must match confirmation password on signup

---

## 📊 Database Schema - Users Table

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at VARCHAR(255)
);
```

---

## 🧪 Complete Test Flow

```bash
# 1. Check health
curl http://localhost:8085/api/auth/health

# 2. Register new user
curl -X POST http://localhost:8085/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "email": "test@example.com",
    "password": "test@123",
    "confirmPassword": "test@123"
  }'

# 3. Login and save token
TOKEN=$(curl -X POST http://localhost:8085/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test_user",
    "password": "test@123"
  }' | jq -r '.token')

# 4. Use token to access API
curl -X GET http://localhost:8085/api/laptops \
  -H "Authorization: Bearer $TOKEN"

# 5. Create new laptop with token
curl -X POST http://localhost:8085/api/laptops \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "brand": "HP",
    "model": "Pavilion",
    "processor": "AMD Ryzen 5",
    "ram": "8GB",
    "storage": "256GB SSD",
    "graphicsCard": "AMD Radeon",
    "price": 599.99,
    "stock": 15,
    "description": "Budget-friendly laptop"
  }'

# 6. Validate token
curl -X POST http://localhost:8085/api/auth/validate \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🆘 Troubleshooting

### "Invalid credentials"
- Check username and password are correct
- Username is case-sensitive

### "401 Unauthorized"
- Token is missing or invalid
- Add `Authorization: Bearer <token>` header
- Token may have expired (24 hours)

### "Username already exists"
- Choose a different username
- Check if user already registered

### "Email already registered"
- Use a different email address

### "Passwords do not match"
- Ensure password and confirmPassword are identical

---

## 🔐 Security Best Practices

1. **Change JWT Secret**: Update `app.jwt.secret` in `application.properties` for production
2. **Secure HTTPS**: Use HTTPS in production
3. **Token Expiration**: Tokens expire after 24 hours
4. **Password Strength**: Encourage strong passwords
5. **Validate Input**: All inputs are validated before processing

---

## 📁 Files Added/Modified

**New Files:**
- `security/JwtTokenProvider.java` - JWT token generation/validation
- `security/JwtAuthenticationFilter.java` - JWT authentication filter
- `security/SecurityConfig.java` - Spring Security configuration
- `model/User.java` - User entity
- `repository/UserRepository.java` - User data access
- `service/AuthService.java` - Authentication business logic
- `controller/AuthController.java` - Authentication endpoints
- `dto/LoginRequest.java` - Login DTO
- `dto/SignupRequest.java` - Signup DTO
- `dto/JwtResponse.java` - JWT response DTO

**Modified Files:**
- `pom.xml` - Added security and JWT dependencies
- `application.properties` - Added JWT configuration

---

## 🚀 Next Steps

1. Ensure MySQL is running and database is created
2. Run: `mvn spring-boot:run`
3. Test signup and login endpoints
4. Get JWT token
5. Use token to access laptop API endpoints

**Your API is now secure with OAuth2 JWT authentication!** 🎉
