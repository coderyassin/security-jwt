package org.yascode.security_jwt.service;

import org.yascode.security_jwt.security.payload.request.ChangePasswordRequest;

import java.security.Principal;

public interface UserService {
    boolean changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser);
}
