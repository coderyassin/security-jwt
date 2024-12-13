package org.yascode.security_jwt.controller.response;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse implements Serializable {
    private Long accountId;
    private Map<String, String> customFields;
}
