package com.selim.lms.dto;

public class LoginRequest {

    private Long userId;  // Only user ID is required
    // No password field here

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
