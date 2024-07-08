package org.yascode.security_jwt.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RefreshTokenRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;

public interface AuthenticationApi {
    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest);
    @PostMapping("/authenticate")
    ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest);
    @PostMapping("/refresh-token")
    ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest);
}
