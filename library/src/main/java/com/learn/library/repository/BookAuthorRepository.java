package com.learn.library.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookAuthorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Assign an author to a book
    public void assignAuthorToBook(Long bookId, Long authorId) {
        String sql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, bookId, authorId);
    }

    // Remove an author from a book
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        String sql = "DELETE FROM book_author WHERE book_id = ? AND author_id = ?";
        jdbcTemplate.update(sql, bookId, authorId);
    }

    // Get all authors for a book
    public List<Long> getAuthorsByBook(Long bookId) {
        String sql = "SELECT author_id FROM book_author WHERE book_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{bookId}, Long.class);
    }

    // Get all books for an author
    public List<Long> getBooksByAuthor(Long authorId) {
        String sql = "SELECT book_id FROM book_author WHERE author_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{authorId}, Long.class);
    }
}
