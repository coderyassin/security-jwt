package org.yascode.security_jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.yascode.security_jwt.security.payload.request.RefreshTokenRequest;
import org.yascode.security_jwt.security.payload.response.RefreshTokenResponse;

public interface RefreshTokenService {
    RefreshTokenResponse generateNewToken(RefreshTokenRequest refreshTokenRequest);
    String getRefreshTokenFromCookies(HttpServletRequest httpServletRequest);
    ResponseCookie refreshTokenCookie(HttpServletRequest httpServletRequest);
    void deleteByToken(String refreshToken);
    ResponseCookie getCleanRefreshTokenCookie();
}
