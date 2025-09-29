package com.selim.lms.dto;

import jakarta.validation.constraints.*;

public class AuthorCreateUpdateDto {

    @NotBlank(message = "Author name is required and cannot be blank")
    @Size(min = 2, max = 120, message = "Author name must be between 2 and 120 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", 
             message = "Author name can only contain letters, spaces, dots, apostrophes, and hyphens")
    private String name;

    public AuthorCreateUpdateDto() {}

    public AuthorCreateUpdateDto(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name != null ? name.trim() : null; }
}
