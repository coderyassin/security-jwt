package org.yascode.security_jwt.service;

public interface AccountService {
    boolean assignCustomFields(Long accountId, String fieldKey, String fieldValue);
}
