package org.yascode.security_jwt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.repository.UserRepository;

@Configuration
@Slf4j
public class ApplicationSecurityConfig {
    private final User userMagic;
    private final UserRepository userRepository;

    public ApplicationSecurityConfig(User userMagic,
                                    UserRepository userRepository) {
        this.userMagic = userMagic;
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService customUserDetailsService(){
        return username -> userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean
    public UserDetailsService magicUserDetailsService(){
        UserDetailsService userDetailsService = username -> {
            if(username.equals(userMagic.getUsername())) {
                return userMagic;
            }
            throw new UsernameNotFoundException("User trying to connect is not a magic user");
        };
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
