# OAuth2 API Documentation

## Base URL
```
http://localhost:8080
```

## OAuth2 Authentication Flow

### 1. Get OAuth2 Login URL
```http
GET /api/auth/oauth2/login
```

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 login URLs",
  "data": {
    "googleLoginUrl": "/oauth2/authorization/google"
  }
}
```

### 2. Start Google OAuth2 Flow
```http
GET /oauth2/authorization/google
```

**Description:** This endpoint redirects to Google's OAuth2 consent screen. After successful authentication, users are redirected to `/api/auth/oauth2-success`.

### 3. OAuth2 Success Handler
```http
GET /api/auth/oauth2-success
```

**Response (New User):**
```json
{
  "success": true,
  "message": "OAuth2 user created and login successful",
  "data": {
    "message": "OAuth2 user created and login successful",
    "user": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@gmail.com",
      "googleId": "123456789",
      "profilePicture": "https://lh3.googleusercontent.com/...",
      "provider": "GOOGLE",
      "createdAt": "2024-01-15T10:30:00",
      "lastLogin": "2024-01-15T10:30:00",
      "isActive": true
    },
    "loginType": "GOOGLE_OAUTH2"
  }
}
```

**Response (Existing User):**
```json
{
  "success": true,
  "message": "OAuth2 login successful",
  "data": {
    "message": "OAuth2 login successful",
    "user": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@gmail.com",
      "googleId": "123456789",
      "profilePicture": "https://lh3.googleusercontent.com/...",
      "provider": "GOOGLE",
      "createdAt": "2024-01-15T10:30:00",
      "lastLogin": "2024-01-15T10:35:00",
      "isActive": true
    },
    "loginType": "GOOGLE_OAUTH2"
  }
}
```

### 4. OAuth2 Failure Handler
```http
GET /api/auth/oauth2-failure
```

**Response:**
```json
{
  "success": false,
  "message": "OAuth2 authentication failed",
  "data": null
}
```

## OAuth2 User Management

### 5. Get All OAuth2 Users
```http
GET /api/auth/oauth2/users
```

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 users retrieved successfully",
  "data": {
    "users": [
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@gmail.com",
        "googleId": "123456789",
        "profilePicture": "https://lh3.googleusercontent.com/...",
        "provider": "GOOGLE",
        "createdAt": "2024-01-15T10:30:00",
        "lastLogin": "2024-01-15T10:35:00",
        "isActive": true
      }
    ],
    "count": 1
  }
}
```

### 6. Get OAuth2 User by Email
```http
GET /api/auth/oauth2/users/{email}
```

**Example:**
```http
GET /api/auth/oauth2/users/john.doe@gmail.com
```

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 user found",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@gmail.com",
    "googleId": "123456789",
    "profilePicture": "https://lh3.googleusercontent.com/...",
    "provider": "GOOGLE",
    "createdAt": "2024-01-15T10:30:00",
    "lastLogin": "2024-01-15T10:35:00",
    "isActive": true
  }
}
```

### 7. Delete OAuth2 User
```http
DELETE /api/auth/oauth2/users/{email}
```

**Example:**
```http
DELETE /api/auth/oauth2/users/john.doe@gmail.com
```

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 user deleted successfully",
  "data": null
}
```

## Testing with Postman

### Step 1: Test OAuth2 Login URL
1. **Method:** GET
2. **URL:** `http://localhost:8080/api/auth/oauth2/login`
3. **Headers:** `Content-Type: application/json`

### Step 2: Test Google OAuth2 Flow
1. **Method:** GET
2. **URL:** `http://localhost:8080/oauth2/authorization/google`
3. **Note:** This will redirect to Google's OAuth2 consent screen

### Step 3: Test OAuth2 Success Handler
1. **Method:** GET
2. **URL:** `http://localhost:8080/api/auth/oauth2-success`
3. **Headers:** `Content-Type: application/json`

### Step 4: Test OAuth2 User Management
1. **Get All Users:** `GET http://localhost:8080/api/auth/oauth2/users`
2. **Get User by Email:** `GET http://localhost:8080/api/auth/oauth2/users/{email}`
3. **Delete User:** `DELETE http://localhost:8080/api/auth/oauth2/users/{email}`

## Complete OAuth2 Flow

1. **User clicks "Login with Google"** → Redirects to `/oauth2/authorization/google`
2. **Google OAuth2 consent screen** → User grants permissions
3. **Google redirects back** → To `/api/auth/oauth2-success`
4. **Application processes OAuth2 data:**
   - If user exists → Updates last login time
   - If new user → Creates new OAuth2 user record
5. **Returns user data** → With login type "GOOGLE_OAUTH2"

## Database Schema

### OAuth2 Users Table (`oauth2_users`)
```sql
CREATE TABLE oauth2_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    google_id VARCHAR(255),
    profile_picture TEXT,
    provider VARCHAR(50) DEFAULT 'GOOGLE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

## Error Responses

### User Not Found
```json
{
  "success": false,
  "message": "OAuth2 user not found",
  "data": null
}
```

### Authentication Failed
```json
{
  "success": false,
  "message": "OAuth2 authentication failed",
  "data": null
}
```

### Server Error
```json
{
  "success": false,
  "message": "Failed to retrieve OAuth2 users: [error details]",
  "data": null
}
```

## CORS Configuration
The backend is configured to accept requests from:
- `http://localhost:3000`
- `http://localhost:3001`
- `http://127.0.0.1:3000`
- `http://localhost:8081`
- `http://localhost:8082` 