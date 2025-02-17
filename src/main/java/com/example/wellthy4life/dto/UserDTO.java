package com.example.wellthy4life.dto;

public class UserDTO {
    private String fullName;
    private String email;
    private String password;
    private String roleName;

    public UserDTO() {}

    public UserDTO(String fullName, String email, String password, String roleName) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roleName = roleName;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
