package org.yascode.security_jwt.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yascode.security_jwt.controller.request.CustomFieldRequest;
import org.yascode.security_jwt.controller.request.CustomFieldsRequest;

public interface AccountApi {
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping("/assign-custom-field")
    ResponseEntity<?> assignCustomField(@RequestBody CustomFieldRequest request);

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping("/assign-custom-fields")
    ResponseEntity<?> assignCustomFields(@RequestBody CustomFieldsRequest request);
}
