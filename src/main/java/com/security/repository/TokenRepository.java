package com.security.repository;

import com.security.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByTokenAndTokenType(String token, Token.TokenType tokenType);
    Optional<Token> findByTokenAndTokenTypeAndUsedFalse(String token, Token.TokenType tokenType);
} 