package com.selim.lms.dto;

import java.time.LocalDate;
import java.util.List;

public class BookDto {
    private Long id;
    private String title;
    private String genre;
    private LocalDate publishDate;
    private List<AuthorBriefDto> authors;

    public BookDto() {}

    public BookDto(Long id, String title, String genre, LocalDate publishDate, List<AuthorBriefDto> authors) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
        this.authors = authors;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public LocalDate getPublishDate() { return publishDate; }
    public List<AuthorBriefDto> getAuthors() { return authors; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    public void setAuthors(List<AuthorBriefDto> authors) { this.authors = authors; }
}

