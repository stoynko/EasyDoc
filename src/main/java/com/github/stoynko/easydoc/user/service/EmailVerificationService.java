package com.github.stoynko.easydoc.user.service;

import com.github.stoynko.easydoc.shared.exception.InvalidTokenException;
import com.github.stoynko.easydoc.user.model.EmailVerificationToken;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.repository.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;

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