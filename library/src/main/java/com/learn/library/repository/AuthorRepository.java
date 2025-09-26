package com.learn.library.repository;

import com.learn.library.model.Author;
import org.springframework.beans.factory.annotation.Autowired; // Autowired is used for automatic dependency injection. It allows Spring to resolve and inject collaborating beans into your bean.
import org.springframework.jdbc.core.JdbcTemplate; // JdbcTemplate is a Spring class that simplifies the use of JDBC (Java Database Connectivity). It handles the creation and release of resources, which helps to avoid common errors such as forgetting to close database connections. It also provides methods for executing SQL queries, updates, and for retrieving results.  
import org.springframework.jdbc.core.RowMapper; // RowMapper is an interface used by JdbcTemplate for mapping rows of a ResultSet on a per-row basis. It allows you to define how each row of the ResultSet should be converted into an object.
import org.springframework.stereotype.Repository; // Repository is a Spring annotation that indicates that the class is a "Repository", which is an abstraction of data access and storage. Spring creates a bean for this class and controls its lifecycle.

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
