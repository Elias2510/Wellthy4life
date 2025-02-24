package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import java.util.List;

public interface AnalysisService {
    Analysis addAnalysis(AnalysisDTO dto);
    Analysis updateAnalysis(Long id, AnalysisDTO dto);
    void deleteAnalysis(Long id);
    List<Analysis> getAnalysesByUser(Long userId);
}
