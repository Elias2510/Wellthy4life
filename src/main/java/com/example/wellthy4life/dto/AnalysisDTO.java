package com.example.wellthy4life.dto;

public class AnalysisDTO {
    private String testName;
    private double value;
    private String unit;
    private Double normalMin;
    private Double normalMax;
    private Long userId;

    public AnalysisDTO() {}

    public AnalysisDTO(String testName, double value, String unit, Double normalMin, Double normalMax, Long userId) {
        this.testName = testName;
        this.value = value;
        this.unit = unit;
        this.normalMin = normalMin;
        this.normalMax = normalMax;
        this.userId = userId;
    }

    public String getTestName() { return testName; }
    public void setTestName(String testName) { this.testName = testName; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getNormalMin() { return normalMin; }
    public void setNormalMin(Double normalMin) { this.normalMin = normalMin; }

    public Double getNormalMax() { return normalMax; }
    public void setNormalMax(Double normalMax) { this.normalMax = normalMax; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}