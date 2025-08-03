package com.security.controller;

import com.security.dto.ApiResponse;
import com.security.dto.ForgotPasswordRequest;
import com.security.dto.LoginRequest;
import com.security.dto.RegistrationRequest;
import com.security.dto.ResetPasswordRequest;
import com.security.dto.OTPVerificationRequest;
import com.security.dto.RegistrationOTPRequest;
import com.security.model.User;
import com.security.service.OAuth2UserService;
import com.security.model.OAuth2User;
import com.security.dto.OAuth2UserResponse;
import com.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:3000", "http://localhost:3000/PanInsight"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private OAuth2UserService oauth2UserService;



    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            // Initiate registration by sending OTP
            userService.initiateRegistration(registrationRequest.getEmail());
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "Registration OTP sent successfully");
            responseData.put("email", registrationRequest.getEmail());
            responseData.put("redirectUrl", "/otp-verification?email=" + registrationRequest.getEmail());
            
            return ResponseEntity.ok(ApiResponse.success("Registration OTP sent successfully", responseData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-registration-otp")
    public ResponseEntity<ApiResponse> verifyRegistrationOTP(@Valid @RequestBody RegistrationOTPRequest request) {
        try {
            // Create RegistrationRequest from the OTP request
            RegistrationRequest registrationRequest = new RegistrationRequest();
            registrationRequest.setFirstName(request.getFirstName());
            registrationRequest.setLastName(request.getLastName());
            registrationRequest.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
            registrationRequest.setGender(request.getGender());
            registrationRequest.setEmail(request.getEmail());
            registrationRequest.setPassword(request.getPassword());
            registrationRequest.setConfirmPassword(request.getConfirmPassword());
            
            // Complete registration with OTP verification
            userService.completeRegistration(request.getOtp(), request.getEmail(), registrationRequest);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "Registration completed successfully");
            responseData.put("email", request.getEmail());
            
            return ResponseEntity.ok(ApiResponse.success("Registration completed successfully", responseData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration verification failed: " + e.getMessage()));
        }
    }

    @PostMapping("/resend-registration-otp")
    public ResponseEntity<ApiResponse> resendRegistrationOTP(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            userService.resendRegistrationOTP(request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Registration OTP sent successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send registration OTP: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest, 
                                           HttpServletRequest request) {
        try {
            System.out.println("Login attempt for email: " + loginRequest.getEmail());
            
            // Check if user exists first
            User user = userService.findByEmail(loginRequest.getEmail());
            if (user == null) {
                System.out.println("User not found with email: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid email or password"));
            }
            
            System.out.println("User found, attempting authentication...");
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            System.out.println("Authentication successful for user: " + loginRequest.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, null);

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("email", user.getEmail());
            userData.put("dateOfBirth", user.getDateOfBirth());
            
            System.out.println("Login successful for user: " + user.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Login successful", userData));
        } catch (Exception e) {
            System.out.println("Login failed for email: " + loginRequest.getEmail() + " - Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();
            
            return ResponseEntity.ok(ApiResponse.success("Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Logout failed: " + e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("User not authenticated"));
            }

            User user = userService.findByEmail(authentication.getName());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User not found"));
            }

            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("email", user.getEmail());
            userData.put("dateOfBirth", user.getDateOfBirth());
            userData.put("createdAt", user.getCreatedAt());

            return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", userData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve profile: " + e.getMessage()));
        }
    }

    @GetMapping("/check-auth")
    public ResponseEntity<ApiResponse> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.ok(ApiResponse.success("User is authenticated"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("User is not authenticated"));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> testEndpoint() {
        return ResponseEntity.ok(ApiResponse.success("Backend is working!"));
    }

    @GetMapping("/debug/user/{email}")
    public ResponseEntity<ApiResponse> debugUser(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("firstName", user.getFirstName());
                userData.put("lastName", user.getLastName());
                userData.put("email", user.getEmail());
                userData.put("dateOfBirth", user.getDateOfBirth());
                userData.put("hasPassword", user.getPassword() != null && !user.getPassword().isEmpty());
                userData.put("passwordLength", user.getPassword() != null ? user.getPassword().length() : 0);
                userData.put("passwordStartsWith", user.getPassword() != null ? user.getPassword().substring(0, Math.min(10, user.getPassword().length())) + "..." : "null");
                return ResponseEntity.ok(ApiResponse.success("User found", userData));
            } else {
                return ResponseEntity.ok(ApiResponse.error("User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error checking user: " + e.getMessage()));
        }
    }

    @PostMapping("/debug/test-password")
    public ResponseEntity<ApiResponse> testPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("User not found"));
            }
            
            // Get the password encoder
            org.springframework.security.crypto.password.PasswordEncoder encoder = 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            
            boolean matches = encoder.matches(password, user.getPassword());
            
            Map<String, Object> result = new HashMap<>();
            result.put("email", email);
            result.put("passwordProvided", password);
            result.put("storedPasswordHash", user.getPassword());
            result.put("passwordMatches", matches);
            result.put("storedPasswordLength", user.getPassword().length());
            
            return ResponseEntity.ok(ApiResponse.success("Password test completed", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error testing password: " + e.getMessage()));
        }
    }

    @DeleteMapping("/debug/delete-user/{email}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.ok(ApiResponse.error("User not found"));
            }
            
            userService.deleteUser(email);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting user: " + e.getMessage()));
        }
    }

    // Email Verification Endpoints
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@Valid @RequestBody OTPVerificationRequest request) {
        try {
            userService.verifyEmail(request.getOtp(), request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Email verified successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Email verification failed: " + e.getMessage()));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse> resendVerificationEmail(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            userService.resendVerificationEmail(request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Verification email sent successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send verification email: " + e.getMessage()));
        }
    }

    // Password Reset Endpoints
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            userService.sendPasswordResetEmail(request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Password reset email sent successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send password reset email: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Password and confirm password do not match"));
            }
            
            userService.resetPassword(request.getOtp(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Password reset failed: " + e.getMessage()));
        }
    }

    // OAuth2 Endpoints
    @GetMapping("/oauth2-success")
    public ResponseEntity<ApiResponse> oauth2Success() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                
                // Extract OAuth2 user information
                Object principal = authentication.getPrincipal();
                String email = null;
                String firstName = null;
                String lastName = null;
                String googleId = null;
                String profilePicture = null;
                
                if (principal instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                    org.springframework.security.oauth2.core.user.OAuth2User oauth2User = 
                        (org.springframework.security.oauth2.core.user.OAuth2User) principal;
                    
                    email = oauth2User.getAttribute("email");
                    firstName = oauth2User.getAttribute("given_name");
                    lastName = oauth2User.getAttribute("family_name");
                    googleId = oauth2User.getName(); // This is the Google ID
                    profilePicture = oauth2User.getAttribute("picture");
                    
                    System.out.println("OAuth2 User Info - Email: " + email + ", Name: " + firstName + " " + lastName);
                }
                
                if (email != null) {
                    // Check if user exists in OAuth2 table
                    Optional<OAuth2User> existingUser = oauth2UserService.findByEmail(email);
                    OAuth2User oauth2User;
                    
                    if (existingUser.isPresent()) {
                        // Update last login for existing user
                        oauth2User = oauth2UserService.updateLastLogin(email);
                        System.out.println("Existing OAuth2 user logged in: " + email);
                    } else {
                        // Create new OAuth2 user
                        oauth2User = oauth2UserService.createOAuth2User(
                            firstName != null ? firstName : "Unknown",
                            lastName != null ? lastName : "User",
                            email,
                            googleId != null ? googleId : "unknown",
                            profilePicture != null ? profilePicture : ""
                        );
                        System.out.println("New OAuth2 user created: " + email);
                    }
                    
                    // Create response data
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("message", "OAuth2 user created and login successful");
                    userData.put("user", new OAuth2UserResponse(
                        oauth2User.getId(),
                        oauth2User.getFirstName(),
                        oauth2User.getLastName(),
                        oauth2User.getEmail(),
                        oauth2User.getGoogleId(),
                        oauth2User.getProfilePicture(),
                        oauth2User.getProvider(),
                        oauth2User.getCreatedAt(),
                        oauth2User.getLastLogin(),
                        oauth2User.getIsActive()
                    ));
                    userData.put("loginType", "GOOGLE_OAUTH2");
                    
                    return ResponseEntity.ok(ApiResponse.success("OAuth2 user created and login successful", userData));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(ApiResponse.error("Could not extract user information from OAuth2"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("OAuth2 authentication failed"));
            }
        } catch (Exception e) {
            System.out.println("OAuth2 success handler error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("OAuth2 success handler failed: " + e.getMessage()));
        }
    }

    @GetMapping("/oauth2-failure")
    public ResponseEntity<ApiResponse> oauth2Failure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("OAuth2 authentication failed"));
    }

    @GetMapping("/oauth2/login")
    public ResponseEntity<ApiResponse> oauth2LoginUrl() {
        Map<String, Object> data = new HashMap<>();
        data.put("googleLoginUrl", "/oauth2/authorization/google");
        return ResponseEntity.ok(ApiResponse.success("OAuth2 login URLs", data));
    }

    // OAuth2 User Management Endpoints
    @GetMapping("/oauth2/users")
    public ResponseEntity<ApiResponse> getAllOAuth2Users() {
        try {
            List<OAuth2User> users = oauth2UserService.getAllOAuth2Users();
            List<OAuth2UserResponse> userResponses = users.stream()
                .map(user -> new OAuth2UserResponse(
                    user.getId(), user.getFirstName(), user.getLastName(), 
                    user.getEmail(), user.getGoogleId(), user.getProfilePicture(),
                    user.getProvider(), user.getCreatedAt(), user.getLastLogin(), 
                    user.getIsActive()
                ))
                .collect(java.util.stream.Collectors.toList());
            
            Map<String, Object> data = new HashMap<>();
            data.put("users", userResponses);
            data.put("count", userResponses.size());
            
            return ResponseEntity.ok(ApiResponse.success("OAuth2 users retrieved successfully", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve OAuth2 users: " + e.getMessage()));
        }
    }

    @GetMapping("/oauth2/users/{email}")
    public ResponseEntity<ApiResponse> getOAuth2UserByEmail(@PathVariable String email) {
        try {
            Optional<OAuth2User> userOpt = oauth2UserService.findByEmail(email);
            if (userOpt.isPresent()) {
                OAuth2User user = userOpt.get();
                OAuth2UserResponse userResponse = new OAuth2UserResponse(
                    user.getId(), user.getFirstName(), user.getLastName(), 
                    user.getEmail(), user.getGoogleId(), user.getProfilePicture(),
                    user.getProvider(), user.getCreatedAt(), user.getLastLogin(), 
                    user.getIsActive()
                );
                
                return ResponseEntity.ok(ApiResponse.success("OAuth2 user found", userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("OAuth2 user not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve OAuth2 user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/oauth2/users/{email}")
    public ResponseEntity<ApiResponse> deleteOAuth2User(@PathVariable String email) {
        try {
            if (oauth2UserService.existsByEmail(email)) {
                oauth2UserService.deleteUser(email);
                return ResponseEntity.ok(ApiResponse.success("OAuth2 user deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("OAuth2 user not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete OAuth2 user: " + e.getMessage()));
        }
    }

} 