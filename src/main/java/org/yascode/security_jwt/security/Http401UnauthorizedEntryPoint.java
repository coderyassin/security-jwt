package org.yascode.security_jwt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.yascode.security_jwt.security.model.ErrorResponse;

import java.io.IOException;
import java.time.Instant;

@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public Http401UnauthorizedEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (sendRedirectToLogin(request, authException)) {
            response.sendRedirect("/login?error=true");
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse body = ErrorResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized")
                .timestamp(Instant.now())
                .message(authException.getMessage())
                .path(request.getServletPath())
                .build();

        objectMapper.writeValue(response.getOutputStream(), body);
    }

    boolean sendRedirectToLogin(HttpServletRequest request, AuthenticationException authException) {
        if (authException instanceof BadCredentialsException && request.getRequestURI().contains("/login")) {
            return request.getRequestURI().contains("/auth") ? false : true;
        }
        return false;
    }
}
