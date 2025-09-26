package com.learn.library.service;

import com.learn.library.model.Review;
import com.learn.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Get all reviews for a book
    public List<Review> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    // Get a single review by ID
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Create a new review
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // Update a review
    public Review updateReview(Review review) {
        return reviewRepository.update(review);
    }

    // Delete a review
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
