package com.example.wellthy4life.dto;

import java.time.LocalDate;

public class UserDTO {
    private String fullName;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String requestedRole; // optional: "ADMIN", "DOCTOR" sau null
    private String accessCode;     // codul introdus pentru rol special

    public UserDTO() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getRequestedRole() { return requestedRole; }
    public void setRequestedRole(String requestedRole) { this.requestedRole = requestedRole; }

    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
}