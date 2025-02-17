package com.example.wellthy4life;

import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.Recommendation;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.RecommendationRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class Wellthy4lifeApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnalysisRepository analysisRepository;

	@Autowired
	private RecommendationRepository recommendationRepository;

	@Test
	public void testSimulateAddingRecommendationForUser() {

		String userEmail = "john.doe@example.com";
		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(userEmail));
		assertThat(optionalUser).isPresent();
		User user = optionalUser.get();


		Optional<Analysis> optionalAnalysis = analysisRepository.findByUserId(user.getId()).stream().findFirst();
		assertThat(optionalAnalysis).isPresent();
		Analysis analysis = optionalAnalysis.get();


		Recommendation recommendation = new Recommendation();
		recommendation.setUser(user);
		recommendation.setAnalysis(analysis);
		recommendation.setRecommendationText("Este recomandat să verificați nivelul de colesterol lunar.");
		recommendationRepository.save(recommendation);

		assertThat(recommendationRepository.findByUserId(user.getId())).isNotEmpty();
		System.out.println("Recommendation added for user: " + user.getFullName());
	}
}
