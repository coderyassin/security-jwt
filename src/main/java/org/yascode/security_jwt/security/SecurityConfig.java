package org.yascode.security_jwt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.security.filter.CostumeCorsFilter;
import org.yascode.security_jwt.security.filter.JwtAuthenticationFilter;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    private final CorsConfigurationSource costumeCorsConfigurationSource;
    private final CostumeCorsFilter costumeCorsFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider customAuthenticationProvider;
    private final MagicAuthenticationProvider magicAuthenticationProvider;
    private final Http401UnauthorizedEntryPoint unauthorizedEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public SecurityConfig(CorsConfigurationSource costumeCorsConfigurationSource,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          AuthenticationProvider customAuthenticationProvider,
                          MagicAuthenticationProvider magicAuthenticationProvider,
                          Http401UnauthorizedEntryPoint unauthorizedEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          CostumeCorsFilter costumeCorsFilter,
                          CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.costumeCorsConfigurationSource = costumeCorsConfigurationSource;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.magicAuthenticationProvider = magicAuthenticationProvider;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.costumeCorsFilter = costumeCorsFilter;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authManager,
                                                   CorsFilter corsFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/v1/auth/**", "/login", "/css/**", "/js/**").permitAll().
                                requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll().
                               requestMatchers("/oauth/**", "/actuator/**", "/favicon.ico").permitAll().
                                requestMatchers(HttpMethod.POST, "/api/v1/resource").hasRole(RoleEnum.ADMIN.name()).
                                anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationManager(authManager)
                .addFilterBefore(costumeCorsFilter, CorsFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        List<AuthenticationProvider> authenticationProviders = List.of(magicAuthenticationProvider, customAuthenticationProvider);
        return new ProviderManager(authenticationProviders);
    }
}
