package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.services.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analyses")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/add")
    public ResponseEntity<AnalysisDTO> addAnalysis(@RequestBody AnalysisDTO dto) {
        Analysis analysis = analysisService.addAnalysis(dto);
        return ResponseEntity.ok(new AnalysisDTO(
                analysis.getUser().getId(),
                analysis.getTestName(),
                analysis.getValue(),
                analysis.getUnit(),
                analysis.getNormalMin(),
                analysis.getNormalMax(),
                analysis.getTestDate()));
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        analysisService.deleteAnalysis(id);
        return ResponseEntity.noContent().build();
    }

    // (Opțional) Endpoint pentru listarea analizelor unui utilizator:
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalysisDTO>> getAnalysesByUser(@PathVariable Long userId) {
        List<Analysis> analyses = analysisService.getAnalysesByUser(userId);
        List<AnalysisDTO> dtoList = analyses.stream().map(a ->
                new AnalysisDTO(a.getUser().getId(), a.getTestName(), a.getValue(), a.getUnit(), a.getNormalMin(), a.getNormalMax(), a.getTestDate())
        ).toList();
        return ResponseEntity.ok(dtoList);
    }
}
