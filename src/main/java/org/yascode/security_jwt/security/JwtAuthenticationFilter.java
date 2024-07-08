package org.yascode.security_jwt.security;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yascode.security_jwt.functionalInterface.TriPredicate;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiPredicate;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final UserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper,
                                   UserDetailsService customUserDetailsService) {
        this.jwtHelper = jwtHelper;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = this.jwtHelper.getJwtFromCookies(request);
        String authorizationHeader = request.getHeader("Authorization");

        TriPredicate<String, String, HttpServletRequest> moveToNextFilter =
                (token, header, httpRequest) -> {
                    if(httpRequest.getRequestURI().contains("/auth"))
                        return true;
                    if(Objects.isNull(token)) {
                        if(Objects.isNull(header) || !header.startsWith("Bearer "))
                            return true;
                    }
                    return false;
                };

        if(moveToNextFilter.test(jwt, authorizationHeader, request)) {
            filterChain.doFilter(request, response);
            return;
        }

        BiPredicate<String, String> jwtPresentInHeader =
                (token, header) -> Objects.nonNull(header) && header.startsWith("Bearer ");

        if(jwtPresentInHeader.test(jwt, authorizationHeader)) {
            jwt = authorizationHeader.substring(7);
        }

        String username = jwtHelper.extractUsername(jwt);

        if(StringUtils.isNotEmpty(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(jwtHelper.isTokenValid(jwt, userDetails)){
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request,response);
    }
}
