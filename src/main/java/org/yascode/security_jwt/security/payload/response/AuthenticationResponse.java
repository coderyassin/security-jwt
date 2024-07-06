package org.yascode.security_jwt.security.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.yascode.security_jwt.enums.AuthorityEnum;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.enums.TokenType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String username;
    private List<RoleEnum> roles;
    private List<AuthorityEnum> authorities;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private TokenType tokenType;
}
