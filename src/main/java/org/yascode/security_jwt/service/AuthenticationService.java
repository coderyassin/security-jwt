package org.yascode.security_jwt.service;

import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;

public interface AuthenticationService {
    StandardResponse register(RegisterRequest request);
    StandardResponse authenticate(AuthenticationRequest authenticationRequest);
}
