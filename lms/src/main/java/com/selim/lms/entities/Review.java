package com.selim.lms.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // BIGSERIAL
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "reviews_user_id_fkey"))
    private User user; // author of the review

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false,
            foreignKey = @ForeignKey(name = "reviews_book_id_fkey"))
    private Book book; // reviewed book

    @Column(columnDefinition = "TEXT")
    private String review;

    protected Review() { }

    public Review(User user, Book book, String review) {
        this.user = user;
        this.book = book;
        this.review = review;
    }

    // getters/setters
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }
}

