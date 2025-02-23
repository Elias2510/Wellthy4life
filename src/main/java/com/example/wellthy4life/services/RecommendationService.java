package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.RecommendationDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.Recommendation;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.RecommendationRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    public Recommendation addRecommendation(RecommendationDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Analysis analysis = analysisRepository.findById(dto.getAnalysisId()).orElseThrow(() -> new RuntimeException("Analysis not found"));

        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.setAnalysis(analysis);
        recommendation.setRecommendationText(dto.getRecommendationText());

        return recommendationRepository.save(recommendation);
    }

    public List<RecommendationDTO> getUserRecommendations(Long userId) {
        List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);
        return recommendations.stream()
                .map(r -> new RecommendationDTO(r.getUser().getId(), r.getAnalysis().getId(), r.getRecommendationText()))
                .collect(Collectors.toList());
    }

    public Recommendation updateRecommendation(Long id, RecommendationDTO dto) {
        Recommendation recommendation = recommendationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));

        recommendation.setRecommendationText(dto.getRecommendationText());
        return recommendationRepository.save(recommendation);
    }

    public void deleteRecommendation(Long id) {
        if (!recommendationRepository.existsById(id)) {
            throw new RuntimeException("Recommendation not found");
        }
        recommendationRepository.deleteById(id);
    }
}