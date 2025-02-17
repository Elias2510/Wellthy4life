package com.example.wellthy4life.repositories;

import com.example.wellthy4life.models.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByUserId(Long userId);
}