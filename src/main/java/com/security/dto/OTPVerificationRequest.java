package com.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OTPVerificationRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "\\d{5}", message = "OTP must be exactly 5 digits")
    private String otp;
    
    // Default constructor
    public OTPVerificationRequest() {}
    
    // Constructor with fields
    public OTPVerificationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
    
    // Getters and Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOtp() {
        return otp;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }
} 