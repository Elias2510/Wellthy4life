// src/main/java/com/example/wellthy4life/dto/PatientDTO.java
package com.example.wellthy4life.dto;

import java.time.LocalDate;

public class PatientDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;

    public PatientDTO() {}

    public PatientDTO(Long id, String fullName, LocalDate birthDate) {
        this.id = id;
        this.fullName = fullName;
        this.birthDate = birthDate;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }

    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}
