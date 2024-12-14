package org.yascode.security_jwt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.response.AuthenticationResponse;
import org.yascode.security_jwt.service.AuthenticationService;

import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String login(@RequestParam(required = false) String redirect_uri, Model model) {
        model.addAttribute("redirectUri", redirect_uri);
        return "login";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> authenticateUser(String username,
                                              String password,
                                              @RequestParam(required = false) String redirect_uri,
                                              HttpServletResponse httpServletResponse) throws IOException {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username(username)
                .password(password)
                .build();
        StandardResponse response = authenticationService.authenticate(authenticationRequest);

        if (redirect_uri != null) {
            if (response.body() instanceof AuthenticationResponse authenticationResponse) {
                httpServletResponse.addCookie(new Cookie("jwt_cookie", authenticationResponse.getAccessToken()));
                httpServletResponse.sendRedirect(redirect_uri);
                return null;
            }

        }

        return ResponseEntity.status(HttpStatus.OK)
                .headers(response.headers())
                .body(response.body());
    }
}
