package org.yascode.security_jwt.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.security.JwtHelper;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtHelperImpl implements JwtHelper {
    private final Log logger = LogFactory.getLog(this.getClass());
    @Value("${application.security.jwt.algorithm}")
    private String algorithm;
    @Value("${application.security.jwt.secret_key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.not_before}")
    private long notBefore;
    @Value("${application.security.jwt.cookie_name}")
    private String jwtCookieName;

    @Override
    public String generateToken(UserDetails userDetails, List<RoleEnum> roles) {

        Map<String, Object> claims = new HashMap<>();
        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.putAll(Map.of("roles", roles, "authorities", authorities));

        try {
            return doGenerateToken(claims, userDetails.getUsername());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from(jwtCookieName, jwt)
                .path("/")
                .maxAge(86400)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return Objects.nonNull(cookie) ? cookie.getValue() : null;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .notBefore(new Date(System.currentTimeMillis() + notBefore))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.forName(algorithm))
                .compact();
    }

    private Key getSigningKey(String _secretKey) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(_secretKey));
    }
}
