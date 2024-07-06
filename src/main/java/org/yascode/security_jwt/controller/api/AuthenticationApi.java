package org.yascode.security_jwt.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.security.payload.response.AuthenticationResponse;

public interface AuthenticationApi {
    @PostMapping("/register")
    ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request);
}
