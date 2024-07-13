package org.yascode.security_jwt.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yascode.security_jwt.controller.response.StandardResponse;
import org.yascode.security_jwt.entity.RefreshToken;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.enums.AuthorityEnum;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.repository.RoleRepository;
import org.yascode.security_jwt.repository.UserRepository;
import org.yascode.security_jwt.security.JwtHelper;
import org.yascode.security_jwt.security.RefreshTokenHelper;
import org.yascode.security_jwt.security.payload.request.AuthenticationRequest;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.security.payload.response.AuthenticationResponse;
import org.yascode.security_jwt.service.AuthenticationService;
import org.yascode.security_jwt.service.RefreshTokenService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.yascode.security_jwt.enums.TokenType.BEARER;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final User userMagic;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final RefreshTokenHelper refreshTokenHelper;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     User userMagic,
                                     UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtHelper jwtHelper,
                                     RefreshTokenHelper refreshTokenHelper,
                                     RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userMagic = userMagic;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.refreshTokenHelper = refreshTokenHelper;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public StandardResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(this.roleRepository.findByRoleEnum(request.getRoles()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        this.userRepository.save(user);

        List<RoleEnum> roles = user.getRoles()
                                   .stream()
                                   .map(Role::getRole)
                                   .toList();

        List<AuthorityEnum> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(AuthorityEnum::valueOf)
                .toList();

        String tokenJwt = this.jwtHelper.generateToken(user, roles);
        RefreshToken refreshToken = null;
        if(Objects.nonNull(user) && Objects.nonNull(user.getId())) {
            refreshToken = this.refreshTokenHelper.createRefreshToken(user.getId());
        }

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .username(user.getUsername())
                .roles(roles)
                .authorities(authorities)
                .accessToken(tokenJwt)
                .refreshToken(Objects.nonNull(refreshToken) ? refreshToken.getToken() : null)
                .tokenType(BEARER)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(Map.of(HttpHeaders.SET_COOKIE, List.of(
                jwtHelper.generateJwtCookie(authenticationResponse.getAccessToken()).toString(),
                refreshTokenHelper.generateRefreshTokenCookie(authenticationResponse.getRefreshToken()).toString())));

        return StandardResponse.builder()
                .headers(headers)
                .body(authenticationResponse)
                .build();
    }

    @Override
    public StandardResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                                        authenticationRequest.getPassword()));
        User user;
        RefreshToken refreshToken;
        if(authenticationRequest.getUsername().equals(userMagic.getUsername())) {
            user = userMagic;
            refreshToken = refreshTokenHelper.createRefreshTokenForUserMagic();
        } else {
            user = userRepository.findByUsername(authenticationRequest.getUsername()).
                    orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
            refreshToken = refreshTokenHelper.createRefreshToken(user.getId());
        }

        List<RoleEnum> roles = user.getRoles()
                .stream()
                .map(Role::getRole)
                .toList();

        List<AuthorityEnum> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(AuthorityEnum::valueOf)
                .toList();

        String jwt = jwtHelper.generateToken(user, roles);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .username(user.getUsername())
                .roles(roles)
                .authorities(authorities)
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .tokenType(BEARER)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(Map.of(HttpHeaders.SET_COOKIE, List.of(
                jwtHelper.generateJwtCookie(authenticationResponse.getAccessToken()).toString(),
                refreshTokenHelper.generateRefreshTokenCookie(authenticationResponse.getRefreshToken()).toString())));

        return StandardResponse.builder()
                .headers(headers)
                .body(authenticationResponse)
                .build();
    }

    @Override
    public Authentication getAuthentication(AuthenticationRequest authenticationRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                                                    authenticationRequest.getPassword()));
    }

    @Override
    public HttpHeaders logout(HttpServletRequest httpServletRequest) {
        String refreshToken = refreshTokenService.getRefreshTokenFromCookies(httpServletRequest);
        if(Objects.nonNull(refreshToken)) {
            refreshTokenService.deleteByToken(refreshToken);
        }
        ResponseCookie jwtCookie = jwtHelper.getCleanJwtCookie();
        ResponseCookie refreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie();

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(Map.of(HttpHeaders.SET_COOKIE, List.of(
                jwtCookie.toString(),
                refreshTokenCookie.toString())));

        return headers;
    }
}
