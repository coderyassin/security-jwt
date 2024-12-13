package org.yascode.security_jwt.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RefreshTokenRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;

public interface AuthenticationApi {
    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest);

    @PostMapping("/authenticate")
    ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest);

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password, HttpServletRequest httpServletRequest);

    @PostMapping("/refresh-token")
    ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest);

    @PostMapping("/refresh-token-cookie")
    ResponseEntity<?> refreshTokenCookie(HttpServletRequest httpServletRequest);

    @GetMapping("/info")
    Authentication getAuthentication(@RequestBody AuthenticationRequest authenticationRequest);

    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpServletRequest httpServletRequest);
}
