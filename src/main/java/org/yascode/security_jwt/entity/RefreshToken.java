package org.yascode.security_jwt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.yascode.security_jwt.listener.RefreshTokenListener;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@EntityListeners(RefreshTokenListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public boolean revoked;
}
