package org.yascode.security_jwt.controller.response;

import lombok.Builder;
import org.springframework.http.HttpHeaders;

@Builder
public record StandardResponse(HttpHeaders headers, Object body) {
}
