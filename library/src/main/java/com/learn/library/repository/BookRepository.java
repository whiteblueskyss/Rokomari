package com.learn.library.repository;

import com.learn.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException; 

import java.util.List;

@Repository
public class BookRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL rows to Book objects
    private RowMapper<Book> bookRowMapper = (rs, rowNum) -> {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setGenre(rs.getString("genre"));
        return book;
    };

    // Get all books
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    // Find book by ID
    public Book findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, bookRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null; // No book found
        }
    }

    // Save a new book
    public Book save(Book book) {
        String sql = "INSERT INTO books (title, genre) VALUES (?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getGenre());
        return book;
    }

    // Update an existing book
    public Book update(Book book) {
        String sql = "UPDATE books SET title = ?, genre = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getGenre(), book.getId());
        return book;
    }

    // Delete a book by ID
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
