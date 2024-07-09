package org.yascode.security_jwt.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yascode.security_jwt.security.payload.request.ChangePasswordRequest;

import java.security.Principal;

public interface UserApi {
    @PatchMapping
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                     Principal connectedUser);
}
