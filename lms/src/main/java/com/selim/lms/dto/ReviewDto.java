package com.selim.lms.dto;

public class ReviewDto {
    private Long id;
    private Long userId;
    private Long bookId;
    private String review;

    public ReviewDto() {}

    public ReviewDto(Long id, Long userId, Long bookId, String review) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.review = review;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public String getReview() { return review; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setReview(String review) { this.review = review; }
}
