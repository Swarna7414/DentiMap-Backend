package com.security.service;

import com.security.model.OTP;
import com.security.repository.OTPRepository;
import com.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;


    @Mock
    private UserRepository userRepository;


    @Mock
    private OTPRepository otpRepository;


    @Mock
    private EmailService emailService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void initiateRegistrationTestingwithexception(){
        String email = "swarna@nana.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            userService.initiateRegistration(email);
        });


        assertEquals("Email Already exists",exception.getMessage());
    }

    @Test
    public void TestwithSuccess(){
        String email = "Swarna@gmai.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        userService.initiateRegistration(email);

        verify(otpRepository,times(1)).save(any(OTP.class));

        verify(emailService,times(1)).sendVerificationEmail(eq(email), anyString());
    }
}