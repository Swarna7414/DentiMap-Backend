package com.security.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class OAuth2UserTest {

    private OAuth2User user;

    @BeforeEach
    public void setup(){
        OAuth2User nonewuser= new OAuth2User();
        user = new OAuth2User("Sai Sankar","Sai","someone@gmail.com","852741");
    }


    @Test
    public void testGettersAndSetters() {
        user.setId(8521L);
        user.setFirstName("Sau Sabjar");
        user.setLastName("Swarna");
        user.setEmail("someone@gmail.com");
        user.setGoogleId("85274");
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setProfilePicture("httpsosnting");
        user.setProvider("Google");
        user.setIsActive(true);



        user.getId();
        user.getFirstName();
        user.getLastName();
        user.getEmail();
        user.getGoogleId();
        user.getCreatedAt();
        user.getLastLogin();
        user.getProfilePicture();
        user.getProvider();
        user.getProvider();
        user.getIsActive();

        user.toString();

        assertEquals(8521L, user.getId());
        assertEquals("Sau Sabjar",user.getFirstName());





    }


}