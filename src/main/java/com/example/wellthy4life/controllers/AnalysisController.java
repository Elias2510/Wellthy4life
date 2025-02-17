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
    public ResponseEntity<Analysis> addAnalysis(@RequestBody AnalysisDTO dto) {
        return ResponseEntity.ok(analysisService.addAnalysis(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalyses(@PathVariable Long userId) {
        return ResponseEntity.ok(analysisService.getUserAnalyses(userId));
    }
}