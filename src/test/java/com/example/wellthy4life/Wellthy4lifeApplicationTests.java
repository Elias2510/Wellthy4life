package com.example.wellthy4life;

import com.example.wellthy4life.models.Role;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.RoleRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Wellthy4lifeApplicationTests {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testCreateUserWithRole() {
		// Verificăm dacă rolul USER există
		Optional<Role> optionalRole = roleRepository.findByName("ADMIN");
		assertThat(optionalRole).isPresent();
		Role userRole = optionalRole.get();

		// Creăm un nou utilizator
		User user = new User();
		user.setFullName("Elias Milosi");
		user.setEmail("jeliasmilosi2002@gmail.com");
		user.setPassword("elias123");

		// Asociem rolul USER cu utilizatorul
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		user.setRoles(roles);

		// Salvăm utilizatorul
		User savedUser = userRepository.save(user);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isGreaterThan(0);
		assertThat(savedUser.getRoles()).isNotEmpty();
		System.out.println("User " + savedUser.getFullName() + " created with role: " + userRole.getName());
	}
}
