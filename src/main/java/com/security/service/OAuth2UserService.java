package com.security.service;

import com.security.model.OAuth2User;
import com.security.repository.OAuth2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OAuth2UserService {

    @Autowired
    private OAuth2UserRepository oauth2UserRepository;

    public OAuth2User createOAuth2User(String firstName, String lastName, String email, String googleId, String profilePicture) {
        OAuth2User oauth2User = new OAuth2User(firstName, lastName, email, googleId);
        oauth2User.setProfilePicture(profilePicture);
        oauth2User.setCreatedAt(LocalDateTime.now());
        oauth2User.setLastLogin(LocalDateTime.now());
        oauth2User.setIsActive(true);
        
        return oauth2UserRepository.save(oauth2User);
    }

    public OAuth2User updateLastLogin(String email) {
        Optional<OAuth2User> userOpt = oauth2UserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            OAuth2User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            return oauth2UserRepository.save(user);
        }
        return null;
    }

    public Optional<OAuth2User> findByEmail(String email) {
        return oauth2UserRepository.findByEmail(email);
    }

    public Optional<OAuth2User> findByGoogleId(String googleId) {
        return oauth2UserRepository.findByGoogleId(googleId);
    }

    public boolean existsByEmail(String email) {
        return oauth2UserRepository.existsByEmail(email);
    }

    public boolean existsByGoogleId(String googleId) {
        return oauth2UserRepository.existsByGoogleId(googleId);
    }

    public List<OAuth2User> getAllOAuth2Users() {
        return oauth2UserRepository.findAll();
    }

    public OAuth2User updateProfilePicture(String email, String profilePicture) {
        Optional<OAuth2User> userOpt = oauth2UserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            OAuth2User user = userOpt.get();
            user.setProfilePicture(profilePicture);
            return oauth2UserRepository.save(user);
        }
        return null;
    }

    public void deactivateUser(String email) {
        Optional<OAuth2User> userOpt = oauth2UserRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            OAuth2User user = userOpt.get();
            user.setIsActive(false);
            oauth2UserRepository.save(user);
        }
    }

    public void deleteUser(String email) {
        oauth2UserRepository.findByEmail(email).ifPresent(oauth2UserRepository::delete);
    }
} 