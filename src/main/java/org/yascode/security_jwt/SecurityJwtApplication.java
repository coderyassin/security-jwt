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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
		/*Authority authority1 = Authority.builder()
									   .authority(AuthorityEnum.ROLE_USER)
									   .build();

		Authority authority2 = Authority.builder()
				.authority(AuthorityEnum.ROLE_ADMIN)
				.build();

		Authority authority3 = Authority.builder()
				.authority(AuthorityEnum.ROLE_SUPER_ADMIN)
				.build();

		authorityRepository.saveAll(List.of(authority1, authority2, authority3));*/

		/*roleRepository.findByRoleEnum(Set.of(RoleEnum.SUPER_ADMIN))
				.stream()
				.findFirst()
				.ifPresent(role -> authorityRepository.findByAuthority(AuthorityEnum.ROLE_SUPER_ADMIN)
						.ifPresent(authority -> {
							Set<Authority> authorities = role.getAuthorities();
							authorities.addAll(List.of(authority));
							role.setAuthorities(authorities);
							roleRepository.save(role);
						}));

		roleRepository.findByRoleEnum(Set.of(RoleEnum.ADMIN))
				.stream()
				.findFirst()
				.ifPresent(role -> authorityRepository.findByAuthority(AuthorityEnum.ROLE_ADMIN)
                        .ifPresent(authority -> {
                            Set<Authority> authorities = role.getAuthorities();
							authorities.addAll(List.of(authority));
                            role.setAuthorities(authorities);
                            roleRepository.save(role);
                        }));

		roleRepository.findByRoleEnum(Set.of(RoleEnum.USER))
				.stream()
				.findFirst()
				.ifPresent(role -> authorityRepository.findByAuthority(AuthorityEnum.ROLE_USER)
						.ifPresent(authority -> {
							Set<Authority> authorities = role.getAuthorities();
							authorities.addAll(List.of(authority));
							role.setAuthorities(authorities);
							roleRepository.save(role);
						}));*/

		/*Optional.ofNullable(authorityRepository.findAll())
				.ifPresent(authorities -> {
					Role role = Role.builder()
							.role(RoleEnum.SUPER_ADMIN)
							.authorities(new HashSet<>(authorities))
							.build();

					roleRepository.save(role);
				});*/

		/*Role role1 = Role.builder()
				.role(RoleEnum.USER)
				.authorities(Set.of(authority))
				.build();

		roleRepository.save(role1);

		User user1 = User.builder()
				.username("yascode")
				.password("YassMel2197@@@@%%%%****5")
				.roles(Set.of(role1))
				.build();

		userRepository.save(user1);*/
	}
}
