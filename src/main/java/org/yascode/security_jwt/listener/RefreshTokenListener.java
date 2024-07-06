package org.yascode.security_jwt.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.yascode.security_jwt.entity.RefreshToken;
import org.yascode.security_jwt.entity.Role;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefreshTokenListener {
    @PrePersist
    void onPrePersist(RefreshToken refreshToken) {
        if(Objects.nonNull(refreshToken) && Objects.isNull(refreshToken.getCreationDate())) {
            refreshToken.setCreationDate(LocalDateTime.now());
        }
    }

    @PreUpdate
    void onPreUpdate(RefreshToken refreshToken) {
        if(Objects.nonNull(refreshToken) && Objects.isNull(refreshToken.getLastModifiedDate())) {
            refreshToken.setLastModifiedDate(LocalDateTime.now());
        }
    }
}
