package com.example.wellthy4life.repositories;

import com.example.wellthy4life.models.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    // Metodă pentru a găsi analizele unui utilizator după userId
    List<Analysis> findByUserId(Long userId);
}
