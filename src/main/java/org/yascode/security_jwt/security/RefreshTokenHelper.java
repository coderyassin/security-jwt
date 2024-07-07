package org.yascode.security_jwt.security;

import org.springframework.http.ResponseCookie;
import org.yascode.security_jwt.entity.RefreshToken;

public interface RefreshTokenHelper {
    RefreshToken createRefreshToken(Long userId);

    ResponseCookie generateRefreshTokenCookie(String refreshToken);
}
