package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.UserRepository;
import com.example.wellthy4life.services.AnalysisService;
import com.example.wellthy4life.util.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/analyses")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @PostMapping("/add")
    public ResponseEntity<AnalysisDTO> addAnalysis(@RequestBody AnalysisDTO dto, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Analysis analysis = new Analysis();
        analysis.setUser(user);
        analysis.setTestName(dto.getTestName());
        analysis.setValue(dto.getValue());
        analysis.setUnit(dto.getUnit());
        analysis.setNormalMin(dto.getNormalMin());
        analysis.setNormalMax(dto.getNormalMax());
        analysis.setTestDate(dto.getTestDate());

        analysisRepository.save(analysis);

        return ResponseEntity.ok(new AnalysisDTO(user.getId(), analysis.getTestName(), analysis.getValue(),
                analysis.getUnit(), analysis.getNormalMin(), analysis.getNormalMax(), analysis.getTestDate()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnalysisDTO> updateAnalysis(@PathVariable Long id, @RequestBody AnalysisDTO dto) {
        Analysis updatedAnalysis = analysisService.updateAnalysis(id, dto);
        return ResponseEntity.ok(new AnalysisDTO(
                updatedAnalysis.getUser().getId(),
                updatedAnalysis.getTestName(),
                updatedAnalysis.getValue(),
                updatedAnalysis.getUnit(),
                updatedAnalysis.getNormalMin(),
                updatedAnalysis.getNormalMax(),
                updatedAnalysis.getTestDate()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalysisDTO>> getAnalysesByUser(@PathVariable Long userId) {
        List<Analysis> analyses = analysisService.getAnalysesByUser(userId);
        List<AnalysisDTO> dtoList = analyses.stream().map(a ->
                new AnalysisDTO(a.getUser().getId(), a.getTestName(), a.getValue(), a.getUnit(), a.getNormalMin(), a.getNormalMax(), a.getTestDate())
        ).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/user")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalyses(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Analysis> analyses = analysisRepository.findByUserId(user.getId());
        List<AnalysisDTO> analysisDTOs = analyses.stream()
                .map(a -> new AnalysisDTO(a.getUser().getId(), a.getTestName(), a.getValue(), a.getUnit(), a.getNormalMin(), a.getNormalMax(), a.getTestDate()))
                .toList();

        return ResponseEntity.ok(analysisDTOs);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        try {
            analysisService.deleteAnalysis(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<AnalysisDTO> getAnalysisById(@PathVariable Long id) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analiza nu a fost găsită"));

        AnalysisDTO analysisDTO = new AnalysisDTO(
                analysis.getUser().getId(),
                analysis.getTestName(),
                analysis.getValue(),
                analysis.getUnit(),
                analysis.getNormalMin(),
                analysis.getNormalMax(),
                analysis.getTestDate()
        );

        return ResponseEntity.ok(analysisDTO);
    }

}
