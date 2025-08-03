package com.security.service;

import com.security.dto.RegistrationRequest;
import com.security.model.User;

public interface UserService {
    
    User registerUser(RegistrationRequest registrationRequest);
    
    User findByEmail(String email);
    
    boolean existsByEmail(String email);
    void deleteUser(String email);
    void sendPasswordResetEmail(String email);
    void resetPassword(String token, String newPassword);
    void verifyEmail(String token);
    void resendVerificationEmail(String email);
} 