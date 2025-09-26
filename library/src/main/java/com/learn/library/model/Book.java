package com.learn.library.model;

import jakarta.validation.constraints.NotEmpty;

public class Book {
    private Long id;

    @NotEmpty(message = "Title must not be empty")
    private String title;

    private String genre;

    public Book() {}

    public Book(Long id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
