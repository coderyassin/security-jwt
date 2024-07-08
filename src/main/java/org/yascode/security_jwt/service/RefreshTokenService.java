package org.yascode.security_jwt.service;

import org.yascode.security_jwt.security.payload.request.RefreshTokenRequest;
import org.yascode.security_jwt.security.payload.response.RefreshTokenResponse;

public interface RefreshTokenService {
    RefreshTokenResponse generateNewToken(RefreshTokenRequest refreshTokenRequest);
}
