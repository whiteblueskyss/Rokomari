package com.learn.library.repository;

import com.learn.library.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL rows to Review objects
    private RowMapper<Review> reviewRowMapper = (rs, rowNum) -> {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setBookId(rs.getLong("book_id"));
        review.setUserId(rs.getLong("user_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        return review;
    };

    // Get all reviews for a book
    public List<Review> findByBookId(Long bookId) {
        String sql = "SELECT * FROM reviews WHERE book_id = ?";
        return jdbcTemplate.query(sql, new Object[]{bookId}, reviewRowMapper);
    }

    // Find review by ID
    public Review findById(Long id) {
        String sql = "SELECT * FROM reviews WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, reviewRowMapper);
    }

    // Save a new review
    public Review save(Review review) {
        String sql = "INSERT INTO reviews (book_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, review.getBookId(), review.getUserId(), review.getRating(), review.getComment());
        return review;
    }

    // Update a review
    public Review update(Review review) {
        String sql = "UPDATE reviews SET rating = ?, comment = ? WHERE id = ?";
        jdbcTemplate.update(sql, review.getRating(), review.getComment(), review.getId());
        return review;
    }

    // Delete a review by ID
    public void deleteById(Long id) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
