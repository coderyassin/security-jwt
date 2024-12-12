package org.yascode.security_jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yascode.security_jwt.controller.api.AccountApi;
import org.yascode.security_jwt.controller.request.CustomFieldRequest;
import org.yascode.security_jwt.service.AccountService;

@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AccountController implements AccountApi {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<?> assignCustomFields(CustomFieldRequest request) {
        return ResponseEntity.ok(accountService.assignCustomFields(request.accountId(),
                request.fieldKey(),
                request.fieldValue()));
    }
}
