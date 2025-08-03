package com.security.repository;

import com.security.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    
    Optional<OTP> findByOtpCodeAndEmailAndOtpTypeAndUsedFalse(String otpCode, String email, OTP.OTPType otpType);
    
    Optional<OTP> findByEmailAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(String email, OTP.OTPType otpType);
    
    void deleteByEmailAndOtpType(String email, OTP.OTPType otpType);
} 