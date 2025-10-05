package com.selim.lms.dto;

import jakarta.validation.constraints.*;


// Two-DTO Pattern - Best Practice Note
// What It Is
// A design pattern where you create separate Data Transfer Objects for incoming requests (input) and outgoing responses (output) in REST APIs.

// Why Use It
// Security: Prevents clients from manipulating sensitive fields like database IDs or timestamps that should only be set by the server.

// Clean Validation: Input DTOs can have strict validation rules while output DTOs don't need any validation since the data is already clean.

// Flexible Design: Input and output can have completely different field requirements without forcing compromises in a single DTO.

// Professional Standard: This is how enterprise applications are built because it separates concerns clearly.

// How It Works  => The input DTO handles data coming from clients with validation and cleaning. The output DTO handles data going to clients with all necessary fields including server-generated ones. This creates a clear boundary between what clients can control versus what they can only read.

// When to Use  => Use this pattern when building any REST API where entities have auto-generated fields, when you need different validation rules for input versus output, or when you want to maintain professional code standards.

// Key Benefit  => It eliminates the complexity of trying to make one DTO serve two different purposes, leading to cleaner, more maintainable, and more secure code.




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
