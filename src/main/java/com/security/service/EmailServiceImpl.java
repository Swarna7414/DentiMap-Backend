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
    public void sendVerificationEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Email Verification - PanInsight");
        
        message.setText(
            "Hello!\n\n" +
            "Thank you for registering with PanInsight. Please verify your email address using the OTP below:\n\n" +
            "Your verification OTP is: " + otp + "\n\n" +
            "This OTP will expire in 10 minutes.\n\n" +
            "If you didn't create an account, please ignore this email.\n\n" +
            "Best regards,\n" +
            "PanInsight Team"
        );
        
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Password Reset Request - PanInsight");
        
        message.setText(
            "Hello!\n\n" +
            "You have requested to reset your password for your PanInsight account.\n\n" +
            "Your password reset OTP is: " + otp + "\n\n" +
            "This OTP will expire in 10 minutes.\n\n" +
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