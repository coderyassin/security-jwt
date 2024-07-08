package org.yascode.security_jwt.security.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.yascode.security_jwt.enums.TokenType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private TokenType tokenType;
}
