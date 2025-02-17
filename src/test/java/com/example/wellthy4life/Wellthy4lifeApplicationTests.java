package com.example.wellthy4life;

import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Wellthy4lifeApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnalysisRepository analysisRepository;

	@Test
	public void testSimulateAddingAnalysisForUser() {
		// Simulăm un utilizator conectat
		String userEmail = "john.doe@example.com";
		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(userEmail));
		assertThat(optionalUser).isPresent();
		User user = optionalUser.get();


		Analysis analysis1 = new Analysis();
		analysis1.setUser(user);
		analysis1.setTestName("Glicemie");
		analysis1.setValue(98.2);
		analysis1.setUnit("mg/dL");
		analysis1.setNormalMin(70.0);
		analysis1.setNormalMax(110.0);
		analysis1.setTestDate(LocalDate.now());
		analysisRepository.save(analysis1);



		assertThat(analysisRepository.findByUserId(user.getId())).hasSizeGreaterThanOrEqualTo(1);
		System.out.println("Added multiple analyses for user: " + user.getFullName());
	}
}