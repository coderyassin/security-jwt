package org.yascode.security_jwt.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.yascode.security_jwt.enums.RoleEnum;

import java.util.List;

public interface JwtHelper {
    String generateToken(UserDetails userDetails, List<RoleEnum> roles);
}
