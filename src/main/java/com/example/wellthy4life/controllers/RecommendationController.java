package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.RecommendationDTO;
import com.example.wellthy4life.models.Recommendation;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.UserRepository;
import com.example.wellthy4life.services.RecommendationService;
import com.example.wellthy4life.util.JWTUtil;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/add")
    public ResponseEntity<RecommendationDTO> addRecommendation(@RequestBody RecommendationDTO dto) {
        Recommendation recommendation = recommendationService.addRecommendation(dto);
        RecommendationDTO responseDto = new RecommendationDTO(
                recommendation.getUser().getId(),
                recommendation.getAnalysis().getId(),
                recommendation.getRecommendationText()
        );
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/user")
    public ResponseEntity<List<RecommendationDTO>> getUserRecommendations(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        List<RecommendationDTO> recommendations = recommendationService.getUserRecommendations(user.getId());
        return ResponseEntity.ok(recommendations);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RecommendationDTO> updateRecommendation(@PathVariable Long id, @RequestBody RecommendationDTO dto) {
        Recommendation updated = recommendationService.updateRecommendation(id, dto);
        return ResponseEntity.ok(new RecommendationDTO(
                updated.getUser().getId(),
                updated.getAnalysis().getId(),
                updated.getRecommendationText()
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.noContent().build();
    }
}