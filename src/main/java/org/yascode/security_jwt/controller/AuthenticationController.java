package org.yascode.security_jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.security_jwt.controller.api.AuthenticationApi;
import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RefreshTokenRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.service.AuthenticationService;
import org.yascode.security_jwt.service.RefreshTokenService;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController implements AuthenticationApi {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        StandardResponse response = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(response.headers())
                .body(response.body());
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        StandardResponse response = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(response.headers())
                .body(response.body());
    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(refreshTokenService.generateNewToken(refreshTokenRequest));
    }

    @Override
    public ResponseEntity<?> refreshTokenCookie(HttpServletRequest httpServletRequest) {
        ResponseCookie newJwtCookie = refreshTokenService.refreshTokenCookie(httpServletRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newJwtCookie.toString())
                .build();
    }

    @Override
    public Authentication getAuthentication(AuthenticationRequest authenticationRequest) {
        return authenticationService.getAuthentication(authenticationRequest);
    }

    @Override
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok()
                .headers(authenticationService.logout(httpServletRequest))
                .build();
    }
}
