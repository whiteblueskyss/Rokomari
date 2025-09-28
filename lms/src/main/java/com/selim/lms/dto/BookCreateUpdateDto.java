package com.selim.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class BookCreateUpdateDto {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 80)
    private String genre;

    // nullable
    private LocalDate publishDate;

    // Optional; when provided, we will validate that all exist
    private List<Long> authorIds;

    public BookCreateUpdateDto() {}

    public BookCreateUpdateDto(String title, String genre, LocalDate publishDate, List<Long> authorIds) {
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
        this.authorIds = authorIds;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public LocalDate getPublishDate() { return publishDate; }
    public List<Long> getAuthorIds() { return authorIds; }

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    public void setAuthorIds(List<Long> authorIds) { this.authorIds = authorIds; }
}
