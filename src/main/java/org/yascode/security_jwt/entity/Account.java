package org.yascode.security_jwt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.yascode.security_jwt.listener.AccountListener;

import java.util.Set;

@Entity
@Table(name = "accounts")
@EntityListeners(AccountListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends AbstractEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "account")
    private Set<CustomField> customFields;
}
