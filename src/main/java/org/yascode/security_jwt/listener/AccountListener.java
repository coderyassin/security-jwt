package org.yascode.security_jwt.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.yascode.security_jwt.entity.Account;

import java.time.LocalDateTime;
import java.util.Objects;

public class AccountListener {
    @PrePersist
    void onPrePersist(Account entity) {
        if(Objects.nonNull(entity) && Objects.isNull(entity.getCreationDate())) {
            entity.setCreationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    void onPreUpdate(Account entity) {
        if(Objects.nonNull(entity) && Objects.isNull(entity.getLastModifiedDate())) {
            entity.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
