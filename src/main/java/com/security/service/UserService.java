package com.security.service;

import com.security.dto.RegistrationRequest;
import com.security.model.User;

public interface UserService {
    
    User registerUser(RegistrationRequest registrationRequest);
    
    User findByEmail(String email);
    
    boolean existsByEmail(String email);
} 