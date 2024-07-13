package org.yascode.security_jwt.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import org.yascode.security_jwt.entity.RefreshToken;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.exception.TokenException;
import org.yascode.security_jwt.repository.RefreshTokenRepository;
import org.yascode.security_jwt.security.JwtHelper;
import org.yascode.security_jwt.security.payload.request.RefreshTokenRequest;
import org.yascode.security_jwt.security.payload.response.RefreshTokenResponse;
import org.yascode.security_jwt.service.RefreshTokenService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static org.yascode.security_jwt.enums.TokenType.BEARER;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final Log logger = LogFactory.getLog(this.getClass());
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHelper jwtHelper;
    private final User userMagic;
    @Value("${application.security.jwt.refresh_token.cookie_name}")
    private String refreshTokenName;
    @Value("${application.security.user.magic.refresh_token.value}")
    private String refreshTokenForUserMagic;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   JwtHelper jwtHelper,
                                   User userMagic) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtHelper = jwtHelper;
        this.userMagic = userMagic;
    }

    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest refreshTokenRequest) {
        User user = refreshTokenRequest.getRefreshToken().equals(refreshTokenForUserMagic) ?
                userMagic : refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenException(refreshTokenRequest.getRefreshToken(), "Refresh token does not exist"));

        List<RoleEnum> roles = user.getRoles()
                .stream()
                .map(Role::getRole)
                .toList();

        String token = jwtHelper.generateToken(user, roles);

        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .tokenType(BEARER)
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest httpServletRequest) {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, refreshTokenName);
        return Objects.nonNull(cookie) ? cookie.getValue() : "";
    }

    @Override
    public ResponseCookie refreshTokenCookie(HttpServletRequest httpServletRequest) {
        String refreshToken = getRefreshTokenFromCookies(httpServletRequest);
        RefreshTokenResponse refreshTokenResponse =
                generateNewToken(RefreshTokenRequest.builder().refreshToken(refreshToken).build());
        return jwtHelper.generateJwtCookie(refreshTokenResponse.getAccessToken());
    }

    @Override
    public void deleteByToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(Objects.nonNull(refreshToken)) {
            if(refreshToken.getExpiryDate().compareTo(Instant.now()) > 0 ) {
                return refreshToken;
            }
            refreshTokenRepository.delete(refreshToken);
            throw new TokenException(refreshToken.getToken(),
                    "Refresh token was expired. Please make a new authentication request");
        }
        throw new TokenException(null, "Token is null");
    }
}
