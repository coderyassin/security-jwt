package org.yascode.security_jwt.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yascode.security_jwt.controller.request.CustomFieldsRequest;
import org.yascode.security_jwt.entity.CustomField;
import org.yascode.security_jwt.repository.AccountRepository;
import org.yascode.security_jwt.repository.CustomFieldRepository;
import org.yascode.security_jwt.service.AccountService;

import java.util.concurrent.atomic.AtomicBoolean;

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
    @Transactional
    public boolean assignCustomFields(CustomFieldsRequest customFields) {
        customFields.customFields()
                .forEach(customField -> assignCustomField(customField.accountId(), customField.fieldKey(), customField.fieldValue()));
        return true;
    }
}
