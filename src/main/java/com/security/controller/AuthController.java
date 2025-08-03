package com.security.controller;

import com.security.dto.ApiResponse;
import com.security.dto.LoginRequest;
import com.security.dto.RegistrationRequest;
import com.security.model.User;
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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://127.0.0.1:3000", "http://localhost:3000/PanInsight"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            User user = userService.registerUser(registrationRequest);
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("firstName", user.getFirstName());
            userData.put("lastName", user.getLastName());
            userData.put("email", user.getEmail());
            userData.put("dateOfBirth", user.getDateOfBirth());
            
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", userData));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
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
} 