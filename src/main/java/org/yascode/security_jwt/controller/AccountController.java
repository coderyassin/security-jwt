package org.yascode.security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.security_jwt.controller.api.AccountApi;
import org.yascode.security_jwt.controller.request.CustomFieldRequest;
import org.yascode.security_jwt.controller.request.CustomFieldsRequest;
import org.yascode.security_jwt.controller.response.AccountResponse;
import org.yascode.security_jwt.service.AccountService;

@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController implements AccountApi {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<AccountResponse> getAccount(Long accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }

    /*@Override
    public AccountResponse getAccount(Long accountId) {
        return accountService.getAccount(accountId);
    }*/

    @Override
    public ResponseEntity<?> assignCustomField(CustomFieldRequest request) {
        return ResponseEntity.ok(accountService.assignCustomField(request.accountId(),
                request.fieldKey(),
                request.fieldValue()));
    }

    @Override
    public ResponseEntity<?> assignCustomFields(CustomFieldsRequest request) {
        return ResponseEntity.ok(accountService.assignCustomFields(request));
    }
}
