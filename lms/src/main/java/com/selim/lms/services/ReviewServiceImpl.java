package com.selim.lms.services;

import com.selim.lms.dto.ReviewCreateUpdateDto;
import com.selim.lms.dto.ReviewDto;
import com.selim.lms.entities.Book;
import com.selim.lms.entities.Review;
import com.selim.lms.entities.User;
import com.selim.lms.exceptions.NotFoundException;
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
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found: " + dto.getUserId()));
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found: " + dto.getBookId()));

        Review review = new Review(user, book, dto.getReview());
        Review saved = reviewRepository.save(review);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDto getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found: " + id));
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
        return reviewRepository.findByBook_Id(bookId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByUser(Long userId) {
        return reviewRepository.findByUser_Id(userId).stream().map(this::toDto).toList();
    }

    @Override
    public ReviewDto update(Long id, ReviewCreateUpdateDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found: " + id));

        // In Phase 2, enforce ownership: only the same user or admin can update.
        // For now (Phase 1), allow.

        // If userId/bookId provided, we allow switching (simple CRUD)
        if (dto.getUserId() != null && !dto.getUserId().equals(review.getUser().getId())) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found: " + dto.getUserId()));
            review.setUser(user);
        }
        if (dto.getBookId() != null && !dto.getBookId().equals(review.getBook().getId())) {
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new NotFoundException("Book not found: " + dto.getBookId()));
            review.setBook(book);
        }

        review.setReview(dto.getReview());
        return toDto(review);
    }

    @Override
    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new NotFoundException("Review not found: " + id);
        }
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
}
