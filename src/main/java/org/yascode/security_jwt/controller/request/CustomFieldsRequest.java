package org.yascode.security_jwt.controller.request;

import java.util.List;

public record CustomFieldsRequest(List<CustomFieldRequest> customFields) {
}
