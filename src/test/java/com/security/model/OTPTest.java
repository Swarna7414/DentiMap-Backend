package com.security.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OTPTest {

    private OTP otp;

    private  OTP otp2=new OTP();

    @BeforeEach
    void setUp() {
        otp = new OTP("252","dfaskdjf@gmail.com", OTP.OTPType.PASSWORD_RESET, LocalDateTime.of(1999,10,15,12,12,20));
        OTP otp1=new OTP();
    }


    @Test
    public void TestGetterandSetters(){
        otp2.setId(8525L);
        otp2.setOtpCode("8522");
        otp2.setEmail("swaranerwa@gmail.com");
        otp2.setOtpType(OTP.OTPType.PASSWORD_RESET);
        otp2.setExpiryDate(LocalDateTime.of(1999,10,15,12,12,20));
        otp2.setUsed(true);
        otp2.setCreatedAt(LocalDateTime.now());



        otp2.getId();
        otp2.getOtpCode();
        otp2.getEmail();
        otp2.getOtpType();
        otp2.getExpiryDate();
        otp2.isUsed();
        otp2.getCreatedAt();
        otp2.isExpired();
    }

}