package com.selim.lms.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 80)
    private String genre;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    // NEW: Many-to-many to authors
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "author_id", nullable = false)
    )
    private Set<Author> authors = new LinkedHashSet<>();

    protected Book() { }

    public Book(String title, String genre, LocalDate publishDate) {
        this.title = title;
        this.genre = genre;
        this.publishDate = publishDate;
    }

    // getters/setters
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }

    public Set<Author> getAuthors() { return authors; }
    public void setAuthors(Set<Author> authors) { this.authors = authors; }

    // convenience helpers to keep both sides in sync
    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }
    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
