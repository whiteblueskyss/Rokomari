package com.selim.lms.dto;

import jakarta.validation.constraints.*;

public class ReviewCreateUpdateDto {

    @NotNull(message = "User ID is required and cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @NotNull(message = "Book ID is required and cannot be null")
    @Positive(message = "Book ID must be a positive number")
    private Long bookId;

    @NotBlank(message = "Review text is required and cannot be blank")
    @Size(min = 10, max = 2000, message = "Review must be between 10 and 2000 characters")
    private String review;

    public ReviewCreateUpdateDto() {}

    public ReviewCreateUpdateDto(Long userId, Long bookId, String review) {
        this.userId = userId;
        this.bookId = bookId;
        this.review = review;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review != null ? review.trim() : null; }
}
