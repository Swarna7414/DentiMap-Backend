# Pan-Backend

A robust Spring Boot backend application for the PanInsight security platform, providing comprehensive authentication, authorization, and user management services.

## Overview

Pan-Backend is a Java-based REST API built with Spring Boot that serves as the core backend for the PanInsight application. It handles user registration, authentication, email verification, OTP management, and secure token-based sessions.

## Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: MySQL
- **Security**: Spring Security with JWT tokens
- **Email Service**: JavaMail API for OTP delivery
- **Build Tool**: Maven
- **API Documentation**: Spring Boot Actuator

## Project Structure

```
Pan-Backend/
├── src/
│   ├── main/
│   │   ├── java/com/security/
│   │   │   ├── config/          
│   │   │   ├── controller/       
│   │   │   ├── dto/             
│   │   │   ├── exception/       
│   │   │   ├── model/           
│   │   │   ├── repository/      
│   │   │   └── service/        
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    
├── pom.xml                      
├── setup-database.sql           
└── fix_otp_table.sql   
```

## Core Features

### Authentication & Authorization
- User registration with email verification
- Secure login with JWT token generation
- Password reset functionality
- OAuth2 integration support
- Role-based access control

### Email Services
- Automated OTP generation and delivery
- Email verification for new registrations
- Password reset email notifications
- Customizable email templates

### Security Features
- JWT-based session management
- Password encryption with BCrypt
- CORS configuration for frontend integration
- Secure token storage and refresh mechanisms

### Data Management
- User profile management
- OTP tracking and expiration
- Token blacklisting for logout
- Audit logging for security events

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh-token` - Token refresh

### Email Verification
- `POST /api/auth/verify-email` - Email verification
- `POST /api/auth/resend-verification` - Resend verification email
- `POST /api/auth/verify-registration-otp` - Registration OTP verification
- `POST /api/auth/resend-registration-otp` - Resend registration OTP

### Password Management
- `POST /api/auth/forgot-password` - Password reset request
- `POST /api/auth/reset-password` - Password reset
- `POST /api/auth/change-password` - Password change

### User Management
- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update user profile
- `DELETE /api/user/account` - Delete user account

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+ or PostgreSQL 12+
- SMTP server access for email functionality

### Key Relationships
- Users can have multiple OTP records
- Tokens are linked to user sessions
- OAuth2 users have separate profile data

## Security Considerations

### Password Security
- Passwords are hashed using BCrypt
- Minimum password requirements enforced
- Password reset requires email verification

### Token Security
- JWT tokens have configurable expiration
- Refresh tokens for extended sessions
- Token blacklisting on logout

### API Security
- CORS configured for frontend integration
- Rate limiting on authentication endpoints
- Input validation and sanitization
- SQL injection prevention through JPA

## Development Guidelines

### Code Structure
- Follow Spring Boot conventions
- Use DTOs for API request/response
- Implement proper exception handling
- Add comprehensive logging

### Testing
- Unit tests for service layer
- Integration tests for controllers
- Database tests with test containers
- Security tests for authentication flows

### Deployment
- Use Docker for containerization
- Configure environment-specific properties
- Set up monitoring and health checks
- Implement proper logging and error tracking


## Common Issues
1. **JWT Issues**: Ensure secret key is properly configured
2. **CORS Errors**: Verify frontend URL in CORS configuration

### Logs
- Application logs are available in console output
- Enable debug logging for detailed troubleshooting
- Monitor database connection pool metrics

## Contributing

1. Follow the existing code structure
2. Add proper documentation for new features
3. Include unit tests for new functionality
4. Update this README for significant changes
5. Follow security best practices

## Support

For technical support or questions about the backend implementation, please refer to the project team- Debesh Jha(https://www.linkedin.com/in/debesh-jha-ph-d-071462aa/), Sai Sankar (https://www.linkedin.com/in/swanra-sai-sankar-000797191/https://www.linkedin.com/in/swanra-sai-sankar-000797191/), Harshith Reddy (https://www.linkedin.com/in/harshith-reddy-nalla-6005012ab/). 