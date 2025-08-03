package com.security.service;

import com.security.model.User;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Loading user by email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        System.out.println("User found: " + user.getEmail());
        System.out.println("User has password: " + (user.getPassword() != null && !user.getPassword().isEmpty()));
        System.out.println("Password length: " + (user.getPassword() != null ? user.getPassword().length() : 0));
        System.out.println("Email verified: " + user.isEmailVerified());
        
        // Optional: Check if email is verified (you can enable this for stricter security)
        // if (!user.isEmailVerified()) {
        //     throw new UsernameNotFoundException("Email not verified for user: " + email);
        // }
        
        return user;
    }
} 