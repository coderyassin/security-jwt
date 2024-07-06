package org.yascode.security_jwt.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.yascode.security_jwt.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserListener {
    @PrePersist
    void onPrePersist(User user) {
        if(Objects.nonNull(user) && Objects.isNull(user.getCreationDate())) {
            user.setCreationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    void onPreUpdate(User user) {
        if(Objects.nonNull(user) && Objects.isNull(user.getLastModifiedDate())) {
            user.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
