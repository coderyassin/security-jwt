package org.yascode.security_jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.yascode.security_jwt.enums.AuthorityEnum;
import org.yascode.security_jwt.listener.AuthorityListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authorities")
@EntityListeners(AuthorityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority extends AbstractEntity implements GrantedAuthority {
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityEnum authority;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authorities")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @Override
    public String getAuthority() {
        return this.authority.name();
    }
}
