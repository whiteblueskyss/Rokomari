package com.selim.lms.dto;

// Though authorDTO and authorBriefDTO are similar, they serve different purposes. AuthorDTO is used for detailed author information, while AuthorBriefDTO is a simplified version for listing authors in a dropdown or summary view. But in this case, still both are identical.

// AuthorDto - Used in:
// /api/authors endpoints (CRUD operations)
// When fetching/creating/updating authors directly

// AuthorBriefDto - Used in:
// BookDto class (line 11: List<AuthorBriefDto> authors)
// When books need to show their authors without full author details


public class AuthorBriefDto {
    private Long id;
    private String name;

    public AuthorBriefDto() {}

    public AuthorBriefDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
