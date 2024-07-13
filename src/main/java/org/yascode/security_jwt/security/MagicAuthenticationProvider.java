package org.yascode.security_jwt.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yascode.security_jwt.entity.User;

@Component
public class MagicAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService magicUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public MagicAuthenticationProvider(UserDetailsService magicUserDetailsService,
                                       PasswordEncoder passwordEncoder) {
        this.magicUserDetailsService = magicUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = (User) magicUserDetailsService.loadUserByUsername(username);

        if(passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        }

        throw new BadCredentialsException("Invalid Credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
