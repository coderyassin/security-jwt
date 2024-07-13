package org.yascode.security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.security_jwt.controller.api.UserApi;
import org.yascode.security_jwt.security.payload.request.ChangePasswordRequest;
import org.yascode.security_jwt.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SUPER_ADMIN')")
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
        return ResponseEntity.ok(userService.changePassword(changePasswordRequest, connectedUser));
    }
}
