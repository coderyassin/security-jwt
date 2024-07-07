package org.yascode.security_jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.security_jwt.controller.api.AuthenticationApi;
import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.service.AuthenticationService;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController implements AuthenticationApi {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
}
