package org.yascode.security_jwt.security.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
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
    private final User userMagic;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${application.security.jwt.refresh_token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh_token.cookie_name}")
    private String refreshTokenName;
    @Value("${application.security.user.magic.refresh_token.value}")
    private String refreshTokenForUserMagic;
    @Value("${application.security.user.magic.refresh_token.expiration}")
    private long refreshTokenForExpirationUserMagic;

    public RefreshTokenHelperImpl(User userMagic,
                                  UserRepository userRepository,
                                  RefreshTokenRepository refreshTokenRepository) {
        this.userMagic = userMagic;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshTokenForUserMagic() {
        return RefreshToken.builder()
                .token(refreshTokenForUserMagic)
                .build();
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

    @Override
    public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(refreshTokenName, refreshToken)
                .path("/")
                .maxAge(refreshExpiration/1000) // 15 days in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }
}
