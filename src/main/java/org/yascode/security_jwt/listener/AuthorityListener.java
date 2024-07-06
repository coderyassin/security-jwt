package org.yascode.security_jwt.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.yascode.security_jwt.entity.Authority;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuthorityListener {
    @PrePersist
    void onPrePersist(Authority authority) {
        if(Objects.nonNull(authority) && Objects.isNull(authority.getCreationDate())) {
            authority.setCreationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    void onPreUpdate(Authority authority) {
        if(Objects.nonNull(authority) && Objects.isNull(authority.getLastModifiedDate())) {
            authority.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
