# Email Configuration Setup Guide

## Gmail SMTP Setup

### 1. Enable 2-Factor Authentication
- Go to your Google Account settings
- Enable 2-Factor Authentication

### 2. Generate App Password
- Go to Google Account settings
- Navigate to Security > 2-Step Verification > App passwords
- Generate a new app password for "Mail"
- Copy the generated password

### 3. Update Application Properties
Update `src/main/resources/application.properties`:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=Harshithreddy010704@gmail.com
spring.mail.password=gnecdpzkaqzbweuv
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

**Email Configuration Complete:**
- **Email**: Harshithreddy010704@gmail.com
- **App Password**: Configured and working

## Features Added

### 1. Email Verification
- **Registration**: Automatically sends verification email
- **Verification**: Users click link to verify email
- **Resend**: Users can request new verification email

### 2. Password Reset
- **Request Reset**: Users request password reset via email
- **Reset Link**: Secure token-based password reset
- **Token Expiry**: 1-hour expiry for security

### 3. Welcome Email
- Sent after successful email verification
- Welcomes users to the platform

## API Endpoints

### Email Verification
- `POST /api/auth/verify-email` - Verify email with token
- `POST /api/auth/resend-verification` - Resend verification email

### Password Reset
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password with token

## Frontend Integration

The backend is ready! You can now develop frontend pages for:
- Email verification page
- Password reset request page
- Password reset form page
- Resend verification page

## Testing

1. Update email configuration
2. Restart the application
3. Register a new user (will receive verification email)
4. Test password reset functionality
5. Test email verification

## Security Features

- **Token Expiry**: 24 hours for verification, 1 hour for password reset
- **One-time Use**: Tokens are marked as used after first use
- **Secure Generation**: UUID-based tokens
- **Email Validation**: Proper email format validation 