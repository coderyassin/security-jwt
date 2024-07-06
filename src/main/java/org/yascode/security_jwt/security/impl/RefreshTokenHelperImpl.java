package org.yascode.security_jwt.security.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yascode.security_jwt.entity.RefreshToken;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.repository.RefreshTokenRepository;
import org.yascode.security_jwt.repository.UserRepository;
import org.yascode.security_jwt.security.RefreshTokenHelper;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Component
public class RefreshTokenHelperImpl implements RefreshTokenHelper {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${application.security.jwt.refresh_token.expiration}")
    private long refreshExpiration;

    public RefreshTokenHelperImpl(UserRepository userRepository,
                                  RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
}
