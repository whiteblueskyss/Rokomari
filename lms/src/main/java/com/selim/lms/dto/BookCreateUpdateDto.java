package com.selim.lms.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class BookCreateUpdateDto {

    @NotBlank(message = "Book title is required and cannot be blank")
    @Size(min = 1, max = 200, message = "Book title must be between 1 and 200 characters")
    private String title;

    @Size(max = 80, message = "Genre should not exceed 80 characters")
    @Pattern(regexp = "^[a-zA-Z\\s-]*$", 
             message = "Genre can only contain letters, spaces, and hyphens")
    private String genre;

    @PastOrPresent(message = "Publish date cannot be in the future")
    private LocalDate publishDate;

    @Size(min = 0, max = 10, message = "A book cannot have more than 10 authors")
    private List<@Positive(message = "Author ID must be a positive number") Long> authorIds;

    public BookCreateUpdateDto() {}

    public BookCreateUpdateDto(String title, String genre, LocalDate publishDate, List<Long> authorIds) {
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
        this.authorIds = authorIds;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title != null ? title.trim() : null; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre != null ? genre.trim() : null; }

    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }

    public List<Long> getAuthorIds() { return authorIds; }
    public void setAuthorIds(List<Long> authorIds) { this.authorIds = authorIds; }
}
