package org.yascode.security_jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;

public interface AuthenticationService {
    StandardResponse register(RegisterRequest request);
    StandardResponse authenticate(AuthenticationRequest authenticationRequest);
    Authentication getAuthentication(AuthenticationRequest authenticationRequest);
    HttpHeaders logout(HttpServletRequest httpServletRequest);
}
