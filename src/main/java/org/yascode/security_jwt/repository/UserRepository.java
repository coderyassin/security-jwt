package org.yascode.security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.security_jwt.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
