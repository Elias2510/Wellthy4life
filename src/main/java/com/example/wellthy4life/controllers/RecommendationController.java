package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.RecommendationDTO;
import com.example.wellthy4life.models.Recommendation;
import com.example.wellthy4life.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/add")
    public ResponseEntity<RecommendationDTO> addRecommendation(@RequestBody RecommendationDTO dto) {
        Recommendation recommendation = recommendationService.addRecommendation(dto);
        RecommendationDTO responseDto = new RecommendationDTO(recommendation.getUser().getId(), recommendation.getAnalysis().getId(), recommendation.getRecommendationText());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecommendationDTO>> getUserRecommendations(@PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.getUserRecommendations(userId));
    }
}