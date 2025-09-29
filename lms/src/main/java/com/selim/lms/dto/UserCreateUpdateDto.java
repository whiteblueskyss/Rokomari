package com.selim.lms.dto;

import jakarta.validation.constraints.*;

public class UserCreateUpdateDto {

    @NotBlank(message = "Name is required and cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", message = "Name can only contain letters, spaces, dots, apostrophes, and hyphens")
    private String name;

    @NotBlank(message = "Email is required and cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 120, message = "Email should not exceed 120 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
             message = "Email must be in valid format (e.g., user@example.com)")
    private String email;

    @NotBlank(message = "Role is required and cannot be blank")
    @Pattern(regexp = "^(READER|ADMIN)$", 
             message = "Role must be either 'READER' or 'ADMIN'")
    private String role;

    public UserCreateUpdateDto() {}

    public UserCreateUpdateDto(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name != null ? name.trim() : null; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email != null ? email.trim().toLowerCase() : null; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role != null ? role.trim().toUpperCase() : null; }
}
