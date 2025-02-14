package com.example.wellthy4life;

import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Wellthy4lifeApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testCreateUser() {
		User user = new User();
		user.setFullName("Ion Popescu");
		user.setEmail("ion.popescu@example.com");
		user.setPassword("parola123");
		user.setBirthDate(java.time.LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0));

		User savedUser = userRepository.save(user);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
}
