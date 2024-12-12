package org.yascode.security_jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yascode.security_jwt.entity.CustomField;

public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {
}