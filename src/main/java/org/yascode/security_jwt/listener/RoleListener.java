package org.yascode.security_jwt.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.entity.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class RoleListener {
    @PrePersist
    void onPrePersist(Role role) {
        if(Objects.nonNull(role) && Objects.isNull(role.getCreationDate())) {
            role.setCreationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    void onPreUpdate(Role role) {
        if(Objects.nonNull(role) && Objects.isNull(role.getLastModifiedDate())) {
            role.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
