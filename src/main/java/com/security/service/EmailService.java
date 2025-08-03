package com.security.service;

public interface EmailService {
    void sendVerificationEmail(String to, String verificationToken);
    void sendPasswordResetEmail(String to, String resetToken);
    void sendWelcomeEmail(String to, String firstName);
} 