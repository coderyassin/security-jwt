package org.yascode.security_jwt.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.yascode.security_jwt.entity.CustomField;

import java.time.LocalDateTime;
import java.util.Objects;

public class CustomFieldListener {
    @PrePersist
    void onPrePersist(CustomField entity) {
        if(Objects.nonNull(entity) && Objects.isNull(entity.getCreationDate())) {
            entity.setCreationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    void onPreUpdate(CustomField entity) {
        if(Objects.nonNull(entity) && Objects.isNull(entity.getLastModifiedDate())) {
            entity.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
