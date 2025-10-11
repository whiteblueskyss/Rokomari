package com.learn.mediconnect.dto;

import jakarta.validation.constraints.*;

public class SpecializationDTO {
    private Long id;
    
    @NotBlank(message = "Specialization name is required")
    private String name;
    
    private String description;

    // Constructors
    public SpecializationDTO() {}

    public SpecializationDTO(String name) {
        this.name = name;
    }

    public SpecializationDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}