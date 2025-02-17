package com.example.wellthy4life;

import com.example.wellthy4life.models.Role;
import com.example.wellthy4life.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Wellthy4lifeApplicationTests {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testCreateRoles() {
		// Listă de roluri de creat
		String[] roleNames = {"USER", "DOCTOR"};

		for (String roleName : roleNames) {
			Optional<Role> existingRole = roleRepository.findByName(roleName);

			if (existingRole.isEmpty()) {
				Role role = new Role();
				role.setName(roleName);

				Role savedRole = roleRepository.save(role);

				assertThat(savedRole).isNotNull();
				assertThat(savedRole.getId()).isGreaterThan(0);
				System.out.println("Role " + roleName + " added with ID: " + savedRole.getId());
			} else {
				System.out.println("Role already exists: " + roleName);
			}
		}
	}
}
