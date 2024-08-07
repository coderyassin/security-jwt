package org.yascode.security_jwt.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

public interface AuthorizationApi {
    @GetMapping("/admin/resource")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE') and hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MAGIC')")
    ResponseEntity<?> sayHelloWithRoleAdminAndReadAuthority();

    @DeleteMapping("/admin/resource")
    @PreAuthorize("hasAuthority('DELETE_PRIVILEGE') and hasAnyRole('SUPER_ADMIN', 'MAGIC')")
    ResponseEntity<?> sayHelloWithRoleAdminAndDeleteAuthority();
}
