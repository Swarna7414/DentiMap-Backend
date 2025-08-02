# Spring Boot Security Backend

A secure Spring Boot 3.2+ backend application with Spring Security, Spring Data JPA, and MySQL database. This application provides user registration, login, and session-based authentication.

## Features

- ✅ User registration with validation
- ✅ Secure login with session-based authentication
- ✅ Password encryption using BCrypt
- ✅ Email uniqueness validation
- ✅ Persistent sessions that don't expire
- ✅ Logout functionality
- ✅ User profile management
- ✅ CORS configuration for frontend integration

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- MySQL server running on localhost:3306

## Database Setup

1. Create a MySQL database named `harshithsecurity`:
```sql
CREATE DATABASE harshithsecurity;
```

2. The application will automatically create the required tables using Hibernate's `ddl-auto=update`.

## Configuration

The application is configured to connect to MySQL with the following settings in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/harshithsecurity
spring.datasource.username=root
spring.datasource.password=Harihara1234
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.servlet.session.timeout=0
```

**Note:** Update the database credentials according to your MySQL setup.

## Running the Application

1. Clone or download the project
2. Navigate to the project directory
3. Run the application using Maven:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication Endpoints

#### 1. User Registration
- **URL:** `POST /api/auth/register`
- **Description:** Register a new user
- **Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01",
  "email": "john.doe@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```
- **Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-01-01"
  }
}
```

#### 2. User Login
- **URL:** `POST /api/auth/login`
- **Description:** Authenticate user and create session
- **Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```
- **Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-01-01"
  }
}
```

#### 3. User Logout
- **URL:** `POST /api/auth/logout`
- **Description:** Destroy user session
- **Response:**
```json
{
  "success": true,
  "message": "Logout successful"
}
```

#### 4. Get User Profile
- **URL:** `GET /api/auth/profile`
- **Description:** Get current user's profile information
- **Authentication:** Required
- **Response:**
```json
{
  "success": true,
  "message": "Profile retrieved successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-01-01",
    "createdAt": "2024-01-01"
  }
}
```

#### 5. Check Authentication Status
- **URL:** `GET /api/auth/check-auth`
- **Description:** Check if user is currently authenticated
- **Response:**
```json
{
  "success": true,
  "message": "User is authenticated"
}
```

## Security Features

- **Password Encryption:** All passwords are encrypted using BCrypt
- **Session Management:** Persistent sessions that don't expire automatically
- **CORS Configuration:** Configured to allow cross-origin requests
- **Input Validation:** Comprehensive validation for all user inputs
- **Error Handling:** Global exception handling with meaningful error messages

## Project Structure

```
src/main/java/com/security/
├── config/
│   └── SecurityConfig.java
├── controller/
│   └── AuthController.java
├── dto/
│   ├── ApiResponse.java
│   ├── LoginRequest.java
│   └── RegistrationRequest.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── service/
│   ├── CustomUserDetailsService.java
│   ├── UserService.java
│   └── UserServiceImpl.java
└── HarshithSecurityBackendApplication.java
```

## Testing the API

You can test the API endpoints using tools like Postman, curl, or any HTTP client.

### Example curl commands:

1. **Register a new user:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-01-01",
    "email": "john.doe@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }'
```

2. **Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }' \
  -c cookies.txt
```

3. **Get profile (with session cookie):**
```bash
curl -X GET http://localhost:8080/api/auth/profile \
  -b cookies.txt
```

4. **Logout:**
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt
```

## Frontend Integration

The backend is configured with CORS to allow frontend integration. The session cookies will be automatically handled by the browser for authenticated requests.

## Notes

- Sessions are persistent and don't expire automatically (`server.servlet.session.timeout=0`)
- Passwords are encrypted using BCrypt with default strength
- Email addresses must be unique across all users
- All endpoints return consistent JSON responses with success/error status
- The application uses Spring Boot 3.2+ with Java 17 