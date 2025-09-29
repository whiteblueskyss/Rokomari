package com.selim.lms.services;

import com.selim.lms.dto.ReviewCreateUpdateDto;
import com.selim.lms.dto.ReviewDto;
import com.selim.lms.entities.Book;
import com.selim.lms.entities.Review;
import com.selim.lms.entities.User;
import com.selim.lms.exceptions.NotFoundException;
import com.selim.lms.exceptions.BusinessValidationException;
import com.selim.lms.exceptions.InvalidDataException;
import com.selim.lms.exceptions.DuplicateResourceException;
import com.selim.lms.repos.BookRepository;
import com.selim.lms.repos.ReviewRepository;
import com.selim.lms.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ReviewDto create(ReviewCreateUpdateDto dto) {
        validateReviewCreateUpdateDto(dto);
        
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + dto.getUserId()));
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + dto.getBookId()));

        // Business rule: Check if user already reviewed this book
        boolean alreadyReviewed = reviewRepository.findByBook_Id(dto.getBookId())
                .stream()
                .anyMatch(existingReview -> existingReview.getUser().getId().equals(dto.getUserId()));
        
        if (alreadyReviewed) {
            throw new DuplicateResourceException(
                "User '" + user.getName() + "' has already reviewed the book '" + book.getTitle() + "'");
        }

        Review review = new Review(user, book, dto.getReview());
        Review saved = reviewRepository.save(review);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDto getById(Long id) {
        validateId(id, "Review ID");
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
        return toDto(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByBook(Long bookId) {
        validateId(bookId, "Book ID");
        
        // Verify book exists
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("Book not found with ID: " + bookId);
        }
        
        return reviewRepository.findByBook_Id(bookId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByUser(Long userId) {
        validateId(userId, "User ID");
        
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        
        return reviewRepository.findByUser_Id(userId).stream().map(this::toDto).toList();
    }

    @Override
    public ReviewDto update(Long id, ReviewCreateUpdateDto dto) {
        validateId(id, "Review ID");
        validateReviewCreateUpdateDto(dto);
        
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));

        // Business rule: In Phase 2, enforce ownership: only the same user or admin can update.
        // For now (Phase 1), we'll allow update but validate user and book exist

        // If userId/bookId provided, we allow switching (simple CRUD)
        if (dto.getUserId() != null && !dto.getUserId().equals(review.getUser().getId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found with ID: " + dto.getUserId()));
            
            // Check if new user already reviewed this book
            boolean alreadyReviewed = reviewRepository.findByBook_Id(review.getBook().getId())
                    .stream()
                    .anyMatch(existingReview -> existingReview.getUser().getId().equals(dto.getUserId()) 
                             && !existingReview.getId().equals(id));
                             
            if (alreadyReviewed) {
                throw new BusinessValidationException(
                    "User '" + user.getName() + "' has already reviewed this book");
            }
            
            review.setUser(user);
        }
        if (dto.getBookId() != null && !dto.getBookId().equals(review.getBook().getId())) {
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new NotFoundException("Book not found with ID: " + dto.getBookId()));
            
            // Check if current user already reviewed the new book
            boolean alreadyReviewed = reviewRepository.findByBook_Id(dto.getBookId())
                    .stream()
                    .anyMatch(existingReview -> existingReview.getUser().getId().equals(review.getUser().getId()) 
                             && !existingReview.getId().equals(id));
                             
            if (alreadyReviewed) {
                throw new BusinessValidationException(
                    "User '" + review.getUser().getName() + "' has already reviewed the book '" + book.getTitle() + "'");
            }
            
            review.setBook(book);
        }

        review.setReview(dto.getReview());
        return toDto(review);
    }

    @Override
    public void delete(Long id) {
        validateId(id, "Review ID");
        
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with ID: " + id));
                
        // Business rule: In Phase 2, enforce ownership: only the same user or admin can delete
        // For now, allow deletion
        
        reviewRepository.deleteById(id);
    }

    private ReviewDto toDto(Review r) {
        return new ReviewDto(
                r.getId(),
                r.getUser().getId(),
                r.getBook().getId(),
                r.getReview()
        );
    }

    // Private validation methods
    private void validateReviewCreateUpdateDto(ReviewCreateUpdateDto dto) {
        if (dto == null) {
            throw new InvalidDataException("Review data cannot be null");
        }
        
        if (dto.getReview() != null && dto.getReview().trim().isEmpty()) {
            throw new InvalidDataException("Review text cannot be empty or contain only whitespace");
        }
    }

    private void validateId(Long id, String fieldName) {
        if (id == null) {
            throw new InvalidDataException(fieldName + " cannot be null");
        }
        if (id <= 0) {
            throw new InvalidDataException(fieldName + " must be a positive number");
        }
    }
}
