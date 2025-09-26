package com.learn.library.service;

import com.learn.library.exception.BookNotFoundException;
import com.learn.library.exception.InvalidReviewException;
import com.learn.library.model.Review;
import com.learn.library.repository.BookRepository;
import com.learn.library.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository; // To check book existence

    // Get all reviews for a book
    public List<Review> getReviewsByBookId(Long bookId) {
        if (bookRepository.findById(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        return reviewRepository.findByBookId(bookId);
    }

    // Get a single review by ID
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Create a new review
    public Review createReview(Review review) {
        if (bookRepository.findById(review.getBookId()) == null) {
            throw new BookNotFoundException(review.getBookId());
        }
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new InvalidReviewException("Rating must be between 1 and 5");
        }
        return reviewRepository.save(review);
    }

    // Update a review
    public Review updateReview(Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new InvalidReviewException("Rating must be between 1 and 5");
        }
        return reviewRepository.update(review);
    }

    // Delete a review
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

}
