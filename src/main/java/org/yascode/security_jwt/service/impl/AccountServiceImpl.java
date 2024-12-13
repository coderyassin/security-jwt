package org.yascode.security_jwt.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yascode.security_jwt.controller.request.CustomFieldsRequest;
import org.yascode.security_jwt.controller.response.AccountResponse;
import org.yascode.security_jwt.entity.Account;
import org.yascode.security_jwt.entity.CustomField;
import org.yascode.security_jwt.repository.AccountRepository;
import org.yascode.security_jwt.repository.CustomFieldRepository;
import org.yascode.security_jwt.service.AccountService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomFieldRepository customFieldRepository;

    public AccountServiceImpl(AccountRepository accountRepository, CustomFieldRepository customFieldRepository) {
        this.accountRepository = accountRepository;
        this.customFieldRepository = customFieldRepository;
    }

    @Override
    public AccountResponse getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .map(this::convertToAccountResponse)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
    }

    @Override
    public boolean assignCustomField(Long accountId, String fieldKey, String fieldValue) {
        AtomicBoolean isAssigned = new AtomicBoolean(false);
        try {
            accountRepository.findById(accountId)
                    .ifPresent(account -> {
                        CustomField customField = CustomField.builder()
                                .account(account)
                                .fieldKey(fieldKey)
                                .fieldValue(fieldValue)
                                .build();

                        customFieldRepository.save(customField);
                        isAssigned.set(true);
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return isAssigned.get();
    }

    @Override
    public boolean assignCustomFields(CustomFieldsRequest customFields) {
        customFields.customFields()
                .forEach(customField -> assignCustomField(customField.accountId(), customField.fieldKey(), customField.fieldValue()));
        return true;
    }

    private AccountResponse convertToAccountResponse(Account account) {
        Map<String, String> customFields = account.getCustomFields().stream()
                .collect(Collectors.toMap(
                        CustomField::getFieldKey,
                        CustomField::getFieldValue
                ));

        return AccountResponse.builder()
                .accountId(account.getId())
                .customFields(customFields)
                .build();
    }
}
