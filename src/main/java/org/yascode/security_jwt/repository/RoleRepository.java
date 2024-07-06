package org.yascode.security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.security_jwt.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
