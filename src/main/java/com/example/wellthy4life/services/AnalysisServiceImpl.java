package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Analysis addAnalysis(AnalysisDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

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

    @Override
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

    @Override
    public void deleteAnalysis(Long id) {
        if (analysisRepository.existsById(id)) {
            analysisRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Analiza nu a fost găsită.");
        }
    }

    @Override
    public List<Analysis> getAnalysesByUser(Long userId) {
        return analysisRepository.findByUserId(userId);
    }
}
