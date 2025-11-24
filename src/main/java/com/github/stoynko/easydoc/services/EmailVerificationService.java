package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.exceptions.InvalidTokenException;
import com.github.stoynko.easydoc.models.EmailVerificationToken;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.repositories.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;

    @Autowired
    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public EmailVerificationToken createTokenForUser(User user) {
        String tokenValue = UUID.randomUUID().toString();
        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(72))
                .used(false)
                .build();

        return tokenRepository.save(token);
    }

    public EmailVerificationToken getValidToken(UUID tokenId) {
        return tokenRepository.findById(tokenId)
                .filter(t -> !t.isUsed())
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElseThrow(InvalidTokenException::new);
    }

    public void markTokenAsUsed(EmailVerificationToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }
}