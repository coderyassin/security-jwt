package org.yascode.security_jwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yascode.security_jwt.entity.Authority;
import org.yascode.security_jwt.entity.Role;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.enums.AuthorityEnum;
import org.yascode.security_jwt.enums.RoleEnum;
import org.yascode.security_jwt.repository.AuthorityRepository;
import org.yascode.security_jwt.repository.RoleRepository;
import org.yascode.security_jwt.repository.UserRepository;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SecurityJwtApplication implements CommandLineRunner {

	private final AuthorityRepository authorityRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

    public SecurityJwtApplication(AuthorityRepository authorityRepository,
                                  RoleRepository roleRepository,
								  UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(SecurityJwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Authority authority1 = Authority.builder()
									   .authority(AuthorityEnum.READ_PRIVILEGE)
									   .build();

		Authority authority2 = Authority.builder()
				.authority(AuthorityEnum.UPDATE_PRIVILEGE)
				.build();

		authorityRepository.saveAll(List.of(authority1, authority2));

		Role role1 = Role.builder()
				.role(RoleEnum.ADMIN)
				.authorities(Set.of(authority1, authority2))
				.build();

		roleRepository.save(role1);

		User user1 = User.builder()
				.username("yascode")
				.password("YassMel2197@@@@%%%%****5")
				.roles(Set.of(role1))
				.build();

		userRepository.save(user1);
	}
}
