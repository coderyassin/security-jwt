package org.yascode.security_jwt.service;

import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.security.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
}
