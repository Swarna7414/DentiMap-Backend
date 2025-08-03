# Complete API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication Endpoints

### 1. User Registration
```http
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01",
  "gender": "Male",
  "email": "john@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "dateOfBirth": "1990-01-01"
  }
}
```

### 2. User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "dateOfBirth": "1990-01-01"
  }
}
```

### 3. User Logout
```http
POST /api/auth/logout
```

### 4. Get User Profile
```http
GET /api/auth/profile
```

### 5. Check Authentication Status
```http
GET /api/auth/check-auth
```

## Email Verification Endpoints

### 6. Verify Email
```http
POST /api/auth/verify-email
Content-Type: application/json

{
  "token": "verification-token-from-email"
}
```

### 7. Resend Verification Email
```http
POST /api/auth/resend-verification
Content-Type: application/json

{
  "email": "john@example.com"
}
```

## Password Reset Endpoints

### 8. Forgot Password
```http
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "john@example.com"
}
```

### 9. Reset Password
```http
POST /api/auth/reset-password
Content-Type: application/json

{
  "token": "reset-token-from-email",
  "password": "newpassword123",
  "confirmPassword": "newpassword123"
}
```

## OAuth2 Endpoints

### 10. Google OAuth2 Login
```http
GET /oauth2/authorization/google
```

### 11. OAuth2 Success Handler
```http
GET /api/auth/oauth2-success
```

### 12. OAuth2 Failure Handler
```http
GET /api/auth/oauth2-failure
```

### 13. Get OAuth2 Login URL
```http
GET /api/auth/oauth2/login
```



## Debug Endpoints (Development Only)

### 14. Test Endpoint
```http
GET /api/auth/test
```

### 15. Debug User
```http
GET /api/auth/debug/user/{email}
```

### 16. Test Password
```http
POST /api/auth/debug/test-password
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### 17. Delete User
```http
DELETE /api/auth/debug/delete-user/{email}
```

## Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    // Response data
  }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

## Error Codes

- `200` - Success
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (authentication required)
- `404` - Not Found
- `500` - Internal Server Error

## Testing

### Using curl
```bash
# Test the backend
curl http://localhost:8080/api/auth/test

# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-01-01",
    "gender": "Male",
    "email": "john@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

### Using Postman
1. Import the endpoints into Postman
2. Set base URL to `http://localhost:8080`
3. Test each endpoint with appropriate data

## Frontend Integration

### CORS Configuration
The backend is configured to accept requests from:
- `http://localhost:3000`
- `http://localhost:3001`
- `http://127.0.0.1:3000`
- `http://localhost:8081`
- `http://localhost:8082`

### Authentication Flow
1. Register user → Receive verification email
2. Verify email → User can login
3. Login → Session created
4. Access protected endpoints with session
5. Logout → Session destroyed

### OAuth2 Flow
1. Redirect to `/oauth2/authorization/google`
2. Complete Google OAuth2 flow
3. Redirected to success/failure handler
4. Session created automatically 