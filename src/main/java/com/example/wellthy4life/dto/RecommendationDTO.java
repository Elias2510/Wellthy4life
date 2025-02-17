package com.example.wellthy4life.dto;

public class RecommendationDTO {
    private Long userId;
    private Long analysisId;
    private String recommendationText;

    public RecommendationDTO() {}

    public RecommendationDTO(Long userId, Long analysisId, String recommendationText) {
        this.userId = userId;
        this.analysisId = analysisId;
        this.recommendationText = recommendationText;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAnalysisId() { return analysisId; }
    public void setAnalysisId(Long analysisId) { this.analysisId = analysisId; }

    public String getRecommendationText() { return recommendationText; }
    public void setRecommendationText(String recommendationText) { this.recommendationText = recommendationText; }
}