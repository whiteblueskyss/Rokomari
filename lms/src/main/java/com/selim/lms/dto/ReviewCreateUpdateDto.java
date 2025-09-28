package com.selim.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewCreateUpdateDto {

    // Phase 1: caller supplies userId.
    // Phase 2: we'll take it from auth principal and drop this field from create.
    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

    @NotBlank
    private String review;

    public ReviewCreateUpdateDto() {}

    public ReviewCreateUpdateDto(Long userId, Long bookId, String review) {
        this.userId = userId;
        this.bookId = bookId;
        this.review = review;
    }

    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public String getReview() { return review; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setReview(String review) { this.review = review; }
}
