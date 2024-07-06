package org.yascode.security_jwt.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yascode.security_jwt.entity.RefreshToken;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.enums.AuthorityEnum;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.repository.AuthorityRepository;
import org.yascode.security_jwt.repository.RoleRepository;
import org.yascode.security_jwt.repository.UserRepository;
import org.yascode.security_jwt.security.JwtHelper;
import org.yascode.security_jwt.security.RefreshTokenHelper;
import org.yascode.security_jwt.security.payload.request.RegisterRequest;
import org.yascode.security_jwt.security.payload.response.AuthenticationResponse;
import org.yascode.security_jwt.service.AuthenticationService;

import java.util.List;
import java.util.Objects;

import static org.yascode.security_jwt.enums.TokenType.BEARER;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final RefreshTokenHelper refreshTokenHelper;
    private final AuthorityRepository authorityRepository;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtHelper jwtHelper,
                                     RefreshTokenHelper refreshTokenHelper,
                                     AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.refreshTokenHelper = refreshTokenHelper;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(this.roleRepository.findByRoleEnum(request.getRoles()))
                .build();

        this.userRepository.save(user);

        List<RoleEnum> roles = user.getRoles().stream().map(Role::getRole).toList();

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
        
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .roles(roles)
                .authorities(authorities)
                .accessToken(tokenJwt)
                .refreshToken(Objects.nonNull(refreshToken) ? refreshToken.getToken() : null)
                .tokenType(BEARER)
                .build();
    }
}
