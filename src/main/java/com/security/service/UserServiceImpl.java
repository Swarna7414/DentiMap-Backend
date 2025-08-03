package com.security.service;

import com.security.dto.RegistrationRequest;
import com.security.model.Token;
import com.security.model.User;
import com.security.repository.TokenRepository;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User registerUser(RegistrationRequest registrationRequest) {
        // Validate password confirmation
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        // Check if email already exists
        if (existsByEmail(registrationRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setDateOfBirth(registrationRequest.getDateOfBirth());
        user.setGender(registrationRequest.getGender());
        user.setEmail(registrationRequest.getEmail());
        
        // Encrypt password
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(encodedPassword);

        // Save user
        User savedUser = userRepository.save(user);

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24); // 24 hours expiry

        // Save token
        Token token = new Token(verificationToken, Token.TokenType.EMAIL_VERIFICATION, savedUser, expiryDate);
        tokenRepository.save(token);

        // Send verification email
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);

        return savedUser;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user = findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1); // 1 hour expiry

        // Save token
        Token token = new Token(resetToken, Token.TokenType.PASSWORD_RESET, user, expiryDate);
        tokenRepository.save(token);

        // Send email
        emailService.sendPasswordResetEmail(email, resetToken);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        Token resetToken = tokenRepository.findByTokenAndTokenTypeAndUsedFalse(token, Token.TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }

    @Override
    public void verifyEmail(String token) {
        Token verificationToken = tokenRepository.findByTokenAndTokenTypeAndUsedFalse(token, Token.TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        if (verificationToken.isExpired()) {
            throw new IllegalArgumentException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark token as used
        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);

        // Send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24); // 24 hours expiry

        // Save token
        Token token = new Token(verificationToken, Token.TokenType.EMAIL_VERIFICATION, user, expiryDate);
        tokenRepository.save(token);

        // Send email
        emailService.sendVerificationEmail(email, verificationToken);
    }
} 