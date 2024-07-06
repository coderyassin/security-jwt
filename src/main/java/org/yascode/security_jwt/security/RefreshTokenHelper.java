package org.yascode.security_jwt.security;

import org.yascode.security_jwt.entity.RefreshToken;

public interface RefreshTokenHelper {
    RefreshToken createRefreshToken(Long userId);
}
