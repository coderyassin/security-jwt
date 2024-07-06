package org.yascode.security_jwt.security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.security.validation.StrongPassword;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull(message = "username must not be null")
    @NotBlank(message = "username must not be blank")
    @NotEmpty(message = "username must not be empty")
    private String username;

    @NotNull(message = "password must not be null")
    @NotBlank(message = "password must not be blank")
    @NotEmpty(message = "password must not be empty")
    @StrongPassword
    private String password;

    /*@NotNull(message = "roles must not be null")
    @NotBlank(message = "role must not be blank")
    @NotEmpty(message = "role must not be empty")*/
    private Set<RoleEnum> roles;
}
