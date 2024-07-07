package org.yascode.security_jwt.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.yascode.security_jwt.enums.RoleEnum;

import java.util.List;

public interface JwtHelper {
    String generateToken(UserDetails userDetails, List<RoleEnum> roles);
    ResponseCookie generateJwtCookie(String jwt);
    String getJwtFromCookies(HttpServletRequest request);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
