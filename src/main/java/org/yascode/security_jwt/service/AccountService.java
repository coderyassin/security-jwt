package org.yascode.security_jwt.service;

import org.yascode.security_jwt.controller.request.CustomFieldsRequest;
import org.yascode.security_jwt.controller.response.AccountResponse;

public interface AccountService {
    AccountResponse getAccount(Long accountId);

    boolean assignCustomField(Long accountId, String fieldKey, String fieldValue);

    boolean assignCustomFields(CustomFieldsRequest customFields);
}
