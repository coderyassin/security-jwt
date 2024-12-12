package org.yascode.security_jwt.service;

import org.yascode.security_jwt.controller.request.CustomFieldsRequest;

public interface AccountService {
    boolean assignCustomField(Long accountId, String fieldKey, String fieldValue);

    boolean assignCustomFields(CustomFieldsRequest customFields);
}
