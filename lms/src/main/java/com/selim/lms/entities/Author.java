package com.selim.lms.entities;

import jakarta.persistence.*;  //Enables annotations like @Entity, @Id, @Column, etc., to map Java classes to database tables. Provides interfaces for managing entities, queries, and transactions (e.g., EntityManager, Query). Supports persistence operations such as saving, updating, and deleting objects in a database.
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    // NEW: inverse side of many-to-many
    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private Set<Book> books = new LinkedHashSet<>();

    protected Author() { }

    public Author(String name) {
        this.name = name;
    }

    // getters/setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Book> getBooks() { return books; }
    public void setBooks(Set<Book> books) { this.books = books; }

    // convenience helpers
    public void addBook(Book book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }
    public void removeBook(Book book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }
}

