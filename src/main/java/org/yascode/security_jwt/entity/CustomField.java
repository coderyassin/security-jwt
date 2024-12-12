package org.yascode.security_jwt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.yascode.security_jwt.listener.CustomFieldListener;

@Entity
@Table(name = "custom_fields")
@EntityListeners(CustomFieldListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomField extends AbstractEntity {
    @Column(name = "field_key")
    private String fieldKey;

    @Column(name = "field_value")
    private String fieldValue;

    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    private Account account;
}
