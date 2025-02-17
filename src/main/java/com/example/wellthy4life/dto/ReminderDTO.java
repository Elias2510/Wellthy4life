package com.example.wellthy4life.dto;

import java.time.LocalDate;

public class ReminderDTO {
    private Long userId;
    private Long analysisId;
    private LocalDate reminderDate;
    private String status;

    public ReminderDTO() {}

    public ReminderDTO(Long userId, Long analysisId, LocalDate reminderDate, String status) {
        this.userId = userId;
        this.analysisId = analysisId;
        this.reminderDate = reminderDate;
        this.status = status;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getAnalysisId() { return analysisId; }
    public void setAnalysisId(Long analysisId) { this.analysisId = analysisId; }

    public LocalDate getReminderDate() { return reminderDate; }
    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
