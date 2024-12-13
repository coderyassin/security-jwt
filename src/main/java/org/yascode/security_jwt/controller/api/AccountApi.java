package org.yascode.security_jwt.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yascode.security_jwt.controller.request.CustomFieldRequest;
import org.yascode.security_jwt.controller.request.CustomFieldsRequest;
import org.yascode.security_jwt.controller.response.AccountResponse;

public interface AccountApi {
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping(value = "/{accountId}")
    ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId);

    /*@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    @GetMapping(value = "/{accountId}")
    AccountResponse getAccount(@PathVariable Long accountId);*/

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping("/assign-custom-field")
    ResponseEntity<?> assignCustomField(@RequestBody CustomFieldRequest request);

    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    @PostMapping("/assign-custom-fields")
    ResponseEntity<?> assignCustomFields(@RequestBody CustomFieldsRequest request);
}
