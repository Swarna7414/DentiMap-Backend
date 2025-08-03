# OAuth2 API Documentation

## Overview
This document provides comprehensive information about the OAuth2 Google authentication endpoints and user management APIs.

## Updated OAuth2 Credentials
- **Client ID**: `879169140311-sa4qr57lch24bt9lcpeqokp410vks92s.apps.googleusercontent.com`
- **Client Secret**: `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`
- **Gmail**: `harshithreddy010704@gmail.com`

## OAuth2 Authentication Flow

### 1. Get OAuth2 Login URL
**Endpoint:** `GET /api/auth/oauth2/login`

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

### 2. Start OAuth2 Flow
**Endpoint:** `GET /oauth2/authorization/google`

**Description:** This endpoint initiates the Google OAuth2 authentication flow. The user will be redirected to Google's consent screen.

### 3. OAuth2 Success Handler
**Endpoint:** `GET /api/auth/oauth2-success`

**Description:** This endpoint is called after successful Google OAuth2 authentication. It creates or updates the user in the `oauth2_users` table.

**Response (New User):**
```json
{
  "success": true,
  "message": "OAuth2 user created and login successful",
  "data": {
    "message": "OAuth2 user created and login successful",
    "user": {
      "id": 1,
      "firstName": "Harshith",
      "lastName": "Reddy",
      "email": "harshithreddy010704@gmail.com",
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
  "message": "OAuth2 user created and login successful",
  "data": {
    "message": "OAuth2 user created and login successful",
    "user": {
      "id": 1,
      "firstName": "Harshith",
      "lastName": "Reddy",
      "email": "harshithreddy010704@gmail.com",
      "googleId": "123456789",
      "profilePicture": "https://lh3.googleusercontent.com/...",
      "provider": "GOOGLE",
      "createdAt": "2024-01-15T10:30:00",
      "lastLogin": "2024-01-15T11:45:00",
      "isActive": true
    },
    "loginType": "GOOGLE_OAUTH2"
  }
}
```

### 4. OAuth2 Failure Handler
**Endpoint:** `GET /api/auth/oauth2-failure`

**Response:**
```json
{
  "success": false,
  "message": "OAuth2 authentication failed"
}
```

## OAuth2 User Management APIs

### 1. Get All OAuth2 Users
**Endpoint:** `GET /api/auth/oauth2/users`

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 users retrieved successfully",
  "data": {
    "users": [
      {
        "id": 1,
        "firstName": "Harshith",
        "lastName": "Reddy",
        "email": "harshithreddy010704@gmail.com",
        "googleId": "123456789",
        "profilePicture": "https://lh3.googleusercontent.com/...",
        "provider": "GOOGLE",
        "createdAt": "2024-01-15T10:30:00",
        "lastLogin": "2024-01-15T11:45:00",
        "isActive": true
      }
    ],
    "count": 1
  }
}
```

### 2. Get OAuth2 User by Email
**Endpoint:** `GET /api/auth/oauth2/users/{email}`

**Example:** `GET /api/auth/oauth2/users/harshithreddy010704@gmail.com`

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 user found",
  "data": {
    "id": 1,
    "firstName": "Harshith",
    "lastName": "Reddy",
    "email": "harshithreddy010704@gmail.com",
    "googleId": "123456789",
    "profilePicture": "https://lh3.googleusercontent.com/...",
    "provider": "GOOGLE",
    "createdAt": "2024-01-15T10:30:00",
    "lastLogin": "2024-01-15T11:45:00",
    "isActive": true
  }
}
```

### 3. Delete OAuth2 User
**Endpoint:** `DELETE /api/auth/oauth2/users/{email}`

**Example:** `DELETE /api/auth/oauth2/users/harshithreddy010704@gmail.com`

**Response:**
```json
{
  "success": true,
  "message": "OAuth2 user deleted successfully"
}
```

## Database Schema

### OAuth2 Users Table (`oauth2_users`)
| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary Key, Auto-increment |
| `first_name` | VARCHAR(255) | User's first name |
| `last_name` | VARCHAR(255) | User's last name |
| `email` | VARCHAR(255) | User's email (Unique) |
| `google_id` | VARCHAR(255) | Google OAuth2 ID |
| `profile_picture` | VARCHAR(500) | Profile picture URL |
| `provider` | VARCHAR(50) | OAuth2 provider (Default: "GOOGLE") |
| `created_at` | DATETIME | Account creation timestamp |
| `last_login` | DATETIME | Last login timestamp |
| `is_active` | BOOLEAN | Account status (Default: true) |

## Testing with Postman

### Step-by-Step Testing:

1. **Start the Application**
   ```bash
   mvn spring-boot:run
   ```

2. **Test OAuth2 Login URL**
   - Method: GET
   - URL: `http://localhost:8080/api/auth/oauth2/login`

3. **Start OAuth2 Flow**
   - Method: GET
   - URL: `http://localhost:8080/oauth2/authorization/google`
   - Note: This will redirect to Google's OAuth2 consent screen

4. **Complete OAuth2 Flow in Browser**
   - Copy the URL from step 3
   - Paste in browser
   - Sign in with Google account
   - Grant permissions

5. **Check Success Handler**
   - Method: GET
   - URL: `http://localhost:8080/api/auth/oauth2-success`

6. **View All OAuth2 Users**
   - Method: GET
   - URL: `http://localhost:8080/api/auth/oauth2/users`

## Frontend Integration

### React Integration Example:
```javascript
// Google OAuth2 Login Button
const handleGoogleLogin = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};

// Check OAuth2 Success
const checkOAuth2Success = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/oauth2-success', {
      method: 'GET',
      credentials: 'include'
    });
    const data = await response.json();
    if (data.success) {
      // User is logged in via OAuth2
      console.log('OAuth2 User:', data.data.user);
    }
  } catch (error) {
    console.error('OAuth2 check failed:', error);
  }
};
```

## Error Handling

### Common Error Responses:

1. **Authentication Failed**
   ```json
   {
     "success": false,
     "message": "OAuth2 authentication failed"
   }
   ```

2. **User Not Found**
   ```json
   {
     "success": false,
     "message": "OAuth2 user not found"
   }
   ```

3. **Server Error**
   ```json
   {
     "success": false,
     "message": "OAuth2 success handler failed: [error details]"
   }
   ```

## Security Features

- ✅ **OAuth2 Standard**: Industry-standard OAuth2 implementation
- ✅ **Separate User Tables**: OAuth2 users stored separately from regular users
- ✅ **Profile Data Extraction**: Automatically extracts Google profile information
- ✅ **Session Management**: Proper session handling for authenticated users
- ✅ **CORS Configuration**: Configured for React frontend integration
- ✅ **User Management**: Full CRUD operations for OAuth2 users

## Next Steps

1. **Test with Postman** - Verify all endpoints work correctly
2. **Frontend Integration** - Update React login button to use OAuth2
3. **Error Handling** - Implement proper error handling in frontend
4. **User Experience** - Add loading states and success/error messages 