package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private UserRepository userRepository;

    public Analysis addAnalysis(AnalysisDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Analysis analysis = new Analysis();
        analysis.setUser(user);
        analysis.setTestName(dto.getTestName());
        analysis.setValue(dto.getValue());
        analysis.setUnit(dto.getUnit());
        analysis.setNormalMin(dto.getNormalMin());
        analysis.setNormalMax(dto.getNormalMax());
        analysis.setTestDate(dto.getTestDate());
        return analysisRepository.save(analysis);
    }

    public List<AnalysisDTO> getUserAnalyses(Long userId) {
        List<Analysis> analyses = analysisRepository.findByUserId(userId);
        return analyses.stream()
                .map(a -> new AnalysisDTO(a.getUser().getId(), a.getTestName(), a.getValue(), a.getUnit(), a.getNormalMin(), a.getNormalMax(), a.getTestDate()))
                .collect(Collectors.toList());
    }

    public Analysis updateAnalysis(Long id, AnalysisDTO dto) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));

        analysis.setTestName(dto.getTestName());
        analysis.setValue(dto.getValue());
        analysis.setUnit(dto.getUnit());
        analysis.setNormalMin(dto.getNormalMin());
        analysis.setNormalMax(dto.getNormalMax());
        analysis.setTestDate(dto.getTestDate());

        return analysisRepository.save(analysis);
    }

    public void deleteAnalysis(Long id) {
        if (!analysisRepository.existsById(id)) {
            throw new RuntimeException("Analysis not found");
        }
        analysisRepository.deleteById(id);
    }

}