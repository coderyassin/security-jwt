package org.yascode.security_jwt.security.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.security.JwtHelper;

import java.security.Key;
import java.util.*;

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
