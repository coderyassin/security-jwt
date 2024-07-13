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
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.functionalInterface.TriPredicate;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiPredicate;

import static org.yascode.security_jwt.util.Constants.JWT_PREFIX;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final UserDetailsService magicUserDetailsService;
    private final UserDetailsService customUserDetailsService;
    private final User userMagic;

    public JwtAuthenticationFilter(JwtHelper jwtHelper,
                                   UserDetailsService magicUserDetailsService,
                                   UserDetailsService customUserDetailsService, User userMagic) {
        this.jwtHelper = jwtHelper;
        this.magicUserDetailsService = magicUserDetailsService;
        this.customUserDetailsService = customUserDetailsService;
        this.userMagic = userMagic;
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
                        if(Objects.isNull(header) || !header.startsWith(JWT_PREFIX))
                            return true;
                    }
                    return false;
                };

        if(moveToNextFilter.test(jwt, authorizationHeader, request)) {
            filterChain.doFilter(request, response);
            return;
        }

        BiPredicate<String, String> jwtPresentInHeader =
                (token, header) -> Objects.nonNull(header) && header.startsWith(JWT_PREFIX);

        if(jwtPresentInHeader.test(jwt, authorizationHeader)) {
            jwt = authorizationHeader.substring(7);
        }

        String username = jwtHelper.extractUsername(jwt);

        if(StringUtils.isNotEmpty(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = username.equals(userMagic.getUsername()) ?
                    magicUserDetailsService.loadUserByUsername(username) :
                    customUserDetailsService.loadUserByUsername(username);

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
