package com.selim.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorCreateUpdateDto {

    @NotBlank
    @Size(max = 120)
    private String name;

    public AuthorCreateUpdateDto() {}

    public AuthorCreateUpdateDto(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
