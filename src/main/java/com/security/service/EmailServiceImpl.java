package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void sendVerificationEmail(String to, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Email Verification - PanInsight");
        
        String verificationUrl = "http://localhost:3000/verify-email?token=" + verificationToken;
        
        message.setText(
            "Hello!\n\n" +
            "Thank you for registering with PanInsight. Please verify your email address by clicking the link below:\n\n" +
            verificationUrl + "\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "If you didn't create an account, please ignore this email.\n\n" +
            "Best regards,\n" +
            "PanInsight Team"
        );
        
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Password Reset Request - PanInsight");
        
        String resetUrl = "http://localhost:3000/reset-password?token=" + resetToken;
        
        message.setText(
            "Hello!\n\n" +
            "You have requested to reset your password for your PanInsight account.\n\n" +
            "Click the link below to reset your password:\n\n" +
            resetUrl + "\n\n" +
            "This link will expire in 1 hour.\n\n" +
            "If you didn't request a password reset, please ignore this email.\n\n" +
            "Best regards,\n" +
            "PanInsight Team"
        );
        
        mailSender.send(message);
    }

    @Override
    public void sendWelcomeEmail(String to, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Welcome to PanInsight!");
        
        message.setText(
            "Hello " + firstName + "!\n\n" +
            "Welcome to PanInsight! Your account has been successfully created and verified.\n\n" +
            "You can now access all our features including:\n" +
            "- AI-powered pancreatic cancer detection\n" +
            "- Medical imaging analysis\n" +
            "- Advanced insights and reports\n\n" +
            "If you have any questions, feel free to contact our support team.\n\n" +
            "Best regards,\n" +
            "PanInsight Team"
        );
        
        mailSender.send(message);
    }
} 