package com.security.service;

import com.security.dto.RegistrationRequest;
import com.security.model.User;

public interface UserService {
    
    User registerUser(RegistrationRequest registrationRequest);
    
    User findByEmail(String email);
    
    boolean existsByEmail(String email);
    void deleteUser(String email);
    void sendPasswordResetEmail(String email);
    void resetPassword(String otp, String email, String newPassword);
    void verifyEmail(String otp, String email);
    void resendVerificationEmail(String email);
    void initiateRegistration(String email);
    void completeRegistration(String otp, String email, RegistrationRequest registrationRequest);
    void resendRegistrationOTP(String email);
    void verifyResetOTP(String otp, String email);
} 