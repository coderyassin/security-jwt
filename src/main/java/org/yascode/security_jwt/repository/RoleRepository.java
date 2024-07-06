package org.yascode.security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.enums.RoleEnum;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT r FROM Role r WHERE r.role IN :roles")
    Set<Role> findByRoleEnum(@Param("roles") Set<RoleEnum> roles);
}
