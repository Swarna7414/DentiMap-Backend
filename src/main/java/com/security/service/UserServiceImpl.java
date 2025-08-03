package com.security.service;

import com.security.dto.RegistrationRequest;
import com.security.model.OTP;
import com.security.model.User;
import com.security.repository.OTPRepository;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public void initiateRegistration(String email) {
        // Check if email already exists
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Generate registration OTP
        String registrationOtp = generateOTP();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // 10 minutes expiry

        // Save OTP
        OTP otp = new OTP(registrationOtp, email, OTP.OTPType.REGISTRATION_VERIFICATION, expiryDate);
        otpRepository.save(otp);

        // Send registration OTP email
        emailService.sendVerificationEmail(email, registrationOtp);
    }

    @Override
    public void completeRegistration(String otp, String email, RegistrationRequest registrationRequest) {
        // Validate password confirmation
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        // Check if email already exists
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Verify OTP
        OTP registrationOtp = otpRepository.findByOtpCodeAndEmailAndOtpTypeAndUsedFalse(otp, email, OTP.OTPType.REGISTRATION_VERIFICATION)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired registration OTP"));

        if (registrationOtp.isExpired()) {
            throw new IllegalArgumentException("Registration OTP has expired");
        }

        // Create new user
        User user = new User();
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setDateOfBirth(registrationRequest.getDateOfBirth());
        user.setGender(registrationRequest.getGender());
        user.setEmail(email);
        
        // Encrypt password
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        user.setPassword(encodedPassword);

        // Set email as verified since OTP was verified
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());

        // Save user
        userRepository.save(user);

        // Mark OTP as used
        registrationOtp.setUsed(true);
        otpRepository.save(registrationOtp);

        // Send welcome email
        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
    }

    @Override
    public void resendRegistrationOTP(String email) {
        // Check if email already exists
        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Generate new registration OTP
        String registrationOtp = generateOTP();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // 10 minutes expiry

        // Save OTP
        OTP otp = new OTP(registrationOtp, email, OTP.OTPType.REGISTRATION_VERIFICATION, expiryDate);
        otpRepository.save(otp);

        // Send registration OTP email
        emailService.sendVerificationEmail(email, registrationOtp);
    }

    @Override
    public User registerUser(RegistrationRequest registrationRequest) {
        // This method is kept for backward compatibility but should not be used
        // Use initiateRegistration and completeRegistration instead
        throw new UnsupportedOperationException("Please use initiateRegistration and completeRegistration methods instead");
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

    // Helper method to generate 5-digit OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000); // Generates 5-digit number between 10000-99999
        return String.valueOf(otp);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        User user = findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        // Generate reset OTP
        String resetOtp = generateOTP();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // 10 minutes expiry

        // Save OTP
        OTP otp = new OTP(resetOtp, email, OTP.OTPType.PASSWORD_RESET, expiryDate);
        otpRepository.save(otp);

        // Send email
        emailService.sendPasswordResetEmail(email, resetOtp);
    }

    @Override
    public void resetPassword(String otp, String email, String newPassword) {
        OTP resetOtp = otpRepository.findByOtpCodeAndEmailAndOtpTypeAndUsedFalse(otp, email, OTP.OTPType.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset OTP"));

        if (resetOtp.isExpired()) {
            throw new IllegalArgumentException("Reset OTP has expired");
        }

        User user = findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Mark OTP as used
        resetOtp.setUsed(true);
        otpRepository.save(resetOtp);
    }

    @Override
    public void verifyEmail(String otp, String email) {
        OTP verificationOtp = otpRepository.findByOtpCodeAndEmailAndOtpTypeAndUsedFalse(otp, email, OTP.OTPType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification OTP"));

        if (verificationOtp.isExpired()) {
            throw new IllegalArgumentException("Verification OTP has expired");
        }

        User user = findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        // Mark OTP as used
        verificationOtp.setUsed(true);
        otpRepository.save(verificationOtp);

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

        // Generate verification OTP
        String verificationOtp = generateOTP();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // 10 minutes expiry

        // Save OTP
        OTP otp = new OTP(verificationOtp, email, OTP.OTPType.EMAIL_VERIFICATION, expiryDate);
        otpRepository.save(otp);

        // Send email
        emailService.sendVerificationEmail(email, verificationOtp);
    }

    @Override
    public void verifyResetOTP(String otp, String email) {
        OTP resetOtp = otpRepository.findByOtpCodeAndEmailAndOtpTypeAndUsedFalse(otp, email, OTP.OTPType.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset OTP"));

        if (resetOtp.isExpired()) {
            throw new IllegalArgumentException("Reset OTP has expired");
        }

        // OTP is valid, but we don't mark it as used yet
        // It will be marked as used when the password is actually reset
    }
} 