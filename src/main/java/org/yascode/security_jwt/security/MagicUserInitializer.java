package org.yascode.security_jwt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.repository.RoleRepository;

import java.util.Set;

@Configuration
@Slf4j
public class MagicUserInitializer {
    private final RoleRepository roleRepository;
    @Value("${application.security.user.magic.username}")
    private String userMagic;
    @Value("${application.security.user.magic.password}")
    private String passwordMagic;
    @Value("${application.security.user.magic.role}")
    private RoleEnum role;

    public MagicUserInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public User userMagic() {
        return User.builder()
                .username(userMagic)
                .password(passwordMagic)
                .roles(roleRepository.findByRoleEnum(Set.of(role)))
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .enabled(true)
                .build();
    }
}
