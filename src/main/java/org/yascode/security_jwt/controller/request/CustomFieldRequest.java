package org.yascode.security_jwt.controller.request;

public record CustomFieldRequest(Long accountId,
                                 String fieldKey,
                                 String fieldValue) {
}
