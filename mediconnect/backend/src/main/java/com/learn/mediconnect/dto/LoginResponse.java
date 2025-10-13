package com.learn.mediconnect.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private String userType;
    private String username;
    private Long userId;
    
    public LoginResponse() {}
    
    public LoginResponse(boolean success, String message, String userType, String username) {
        this.success = success;
        this.message = message;
        this.userType = userType;
        this.username = username;
    }
    
    public LoginResponse(boolean success, String message, String userType, String username, Long userId) {
        this.success = success;
        this.message = message;
        this.userType = userType;
        this.username = username;
        this.userId = userId;
    }
    
    // Static factory methods
    public static LoginResponse success(String userType, String username) {
        return new LoginResponse(true, "Login successful", userType, username);
    }
    
    public static LoginResponse success(String userType, String username, Long userId) {
        return new LoginResponse(true, "Login successful", userType, username, userId);
    }
    
    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null);
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}