package org.yascode.security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.security_jwt.controller.api.AuthenticationApi;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.security.payload.response.AuthenticationResponse;
import org.yascode.security_jwt.service.AuthenticationService;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController implements AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        return ResponseEntity.ok(authenticationResponse);
    }
}
