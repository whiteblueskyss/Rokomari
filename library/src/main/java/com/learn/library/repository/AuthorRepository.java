package com.learn.library.repository;

import com.learn.library.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL rows to Author objects
    private RowMapper<Author> authorRowMapper = (rs, rowNum) -> {
        Author author = new Author();
        author.setId(rs.getLong("id"));
        author.setName(rs.getString("name"));
        return author;
    };

    // Get all authors
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        return jdbcTemplate.query(sql, authorRowMapper);
    }

    // Find author by ID
    public Author findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, authorRowMapper);
    }

    // Save a new author
    public Author save(Author author) {
        String sql = "INSERT INTO authors (name) VALUES (?)";
        jdbcTemplate.update(sql, author.getName());
        return author;
    }

    // Update an existing author
    public Author update(Author author) {
        String sql = "UPDATE authors SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, author.getName(), author.getId());
        return author;
    }

    // Delete an author by ID
    public void deleteById(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
