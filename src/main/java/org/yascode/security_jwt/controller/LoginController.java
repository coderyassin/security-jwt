package org.yascode.security_jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.service.AuthenticationService;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> authenticateUser(String username, String password, HttpServletRequest httpServletRequest) {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build();
        StandardResponse response = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .headers(response.headers())
                .body(response.body());
    }
}
