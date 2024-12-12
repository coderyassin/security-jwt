package org.yascode.security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.security_jwt.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}