package org.yascode.security_jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.listener.RoleListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@EntityListeners(RoleListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends AbstractEntity {
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}
