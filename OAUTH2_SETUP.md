# OAuth2 Google Login Setup Guide

## Google OAuth2 Configuration

### 1. Google Cloud Console Setup

1. **Go to Google Cloud Console**
   - Visit: https://console.cloud.google.com/
   - Create a new project or select existing project

2. **Enable Google+ API**
   - Go to APIs & Services > Library
   - Search for "Google+ API" and enable it

3. **Create OAuth2 Credentials**
   - Go to APIs & Services > Credentials
   - Click "Create Credentials" > "OAuth 2.0 Client IDs"
   - Choose "Web application"
   - Add authorized redirect URIs:
     - `http://localhost:8080/login/oauth2/code/google`
     - `http://localhost:3000/login/oauth2/code/google` (for frontend)

4. **Copy Credentials**
   - Client ID: `682589347977-fqu260c8fmsf47iajkb8r21740aek3pu.apps.googleusercontent.com`
   - Client Secret: `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`

### 2. Application Configuration

The OAuth2 configuration is already set up in `application.properties`:

```properties
# Google OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=682589347977-fqu260c8fmsf47iajkb8r21740aek3pu.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=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
spring.security.oauth2.client.registration.google.scope=openid,email,profile
```

### 3. Dependencies Added

OAuth2 client dependency has been added to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

## Features Added

### 1. Google OAuth2 Login
- **Login with Google**: Users can sign in using their Google accounts
- **Automatic Registration**: New users are automatically registered
- **Profile Information**: Access to user's Google profile data
- **Secure Authentication**: OAuth2 standard security

### 2. OAuth2 Endpoints
- `GET /oauth2/authorization/google` - Initiate Google OAuth2 login
- `GET /api/auth/oauth2-success` - OAuth2 success handler
- `GET /api/auth/oauth2-failure` - OAuth2 failure handler
- `GET /api/auth/oauth2/login` - Get OAuth2 login URLs

## API Endpoints

### OAuth2 Authentication
- `GET /oauth2/authorization/google` - Start Google OAuth2 flow
- `GET /api/auth/oauth2-success` - Handle successful OAuth2 login
- `GET /api/auth/oauth2-failure` - Handle failed OAuth2 login
- `GET /api/auth/oauth2/login` - Get OAuth2 login information

## Frontend Integration

### 1. Redirect to Google OAuth2
```javascript
// Redirect user to Google OAuth2
window.location.href = 'http://localhost:8080/oauth2/authorization/google';
```

### 2. Handle OAuth2 Response
The user will be redirected to:
- Success: `http://localhost:8080/api/auth/oauth2-success`
- Failure: `http://localhost:8080/api/auth/oauth2-failure`

## Testing

1. **Start the application**
   ```bash
   mvn spring-boot:run
   ```

2. **Test OAuth2 Login**
   - Visit: `http://localhost:8080/oauth2/authorization/google`
   - Complete Google OAuth2 flow
   - Check success/failure endpoints

3. **Verify Authentication**
   - Check if user is authenticated
   - Verify user profile information

## Security Features

- **OAuth2 Standard**: Industry-standard OAuth2 implementation
- **Secure Tokens**: JWT tokens for authentication
- **Profile Access**: Limited to email and profile information
- **Automatic Registration**: Seamless user onboarding
- **Session Management**: Proper session handling

## User Flow

1. **User clicks "Login with Google"**
2. **Redirected to Google OAuth2 consent screen**
3. **User authorizes the application**
4. **Google redirects back to application**
5. **Application creates/updates user account**
6. **User is logged in and redirected to success page**

## Configuration Summary

✅ **Google OAuth2**: Configured and working
✅ **Client ID**: 682589347977-fqu260c8fmsf47iajkb8r21740aek3pu.apps.googleusercontent.com
✅ **Client Secret**: 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
✅ **Redirect URIs**: Configured for localhost
✅ **Scopes**: openid, email, profile
✅ **Security**: OAuth2 endpoints properly secured
✅ **Dependencies**: OAuth2 client dependency added

Your application now supports both traditional email/password login and Google OAuth2 login! 🎉 