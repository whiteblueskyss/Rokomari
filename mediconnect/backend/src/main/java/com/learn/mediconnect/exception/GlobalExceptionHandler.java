package com.learn.mediconnect.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors (DTO validation failures)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
            "VALIDATION_ERROR",
            "Validation failed for one or more fields",
            LocalDateTime.now(),
            errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle resource not found errors (IllegalArgumentException from services)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "RESOURCE_NOT_FOUND",
            ex.getMessage(),
            LocalDateTime.now(),
            null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle database constraint violations (duplicate email, username, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Data integrity violation occurred";
        
        // Log the full exception for debugging
        System.err.println("DataIntegrityViolationException: " + ex.getMessage());
        if (ex.getCause() != null) {
            System.err.println("Caused by: " + ex.getCause().getMessage());
        }
        
        // Extract meaningful message from the exception
        String fullMessage = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            fullMessage += " " + ex.getCause().getMessage().toLowerCase();
        }
        
        if (fullMessage.contains("unique constraint") || fullMessage.contains("duplicate key")) {
            if (fullMessage.contains("email")) {
                message = "Email address already exists";
            } else if (fullMessage.contains("username")) {
                message = "Username already exists";
            } else if (fullMessage.contains("uk_") || fullMessage.contains("unique")) {
                message = "Duplicate entry found - this value already exists";
            } else {
                message = "Duplicate entry found";
            }
        } else if (fullMessage.contains("foreign key") || fullMessage.contains("fk_")) {
            message = "Cannot perform operation - referenced data does not exist";
        } else if (fullMessage.contains("not null") || fullMessage.contains("null value")) {
            message = "Required field cannot be empty";
        } else {
            // Include more details for debugging
            message = "Data integrity violation: " + (ex.getMessage() != null ? ex.getMessage() : "Unknown constraint violation");
        }

        ErrorResponse errorResponse = new ErrorResponse(
            "DATA_INTEGRITY_ERROR",
            message,
            LocalDateTime.now(),
            null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Handle any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred. Please contact support.",
            LocalDateTime.now(),
            null
        );

        // Log the actual exception for debugging (in real app, use proper logging)
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Standard error response class
    public static class ErrorResponse {
        private String code;
        private String message;
        private LocalDateTime timestamp;
        private Map<String, String> details;

        public ErrorResponse(String code, String message, LocalDateTime timestamp, Map<String, String> details) {
            this.code = code;
            this.message = message;
            this.timestamp = timestamp;
            this.details = details;
        }

        // Getters and setters
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public Map<String, String> getDetails() {
            return details;
        }

        public void setDetails(Map<String, String> details) {
            this.details = details;
        }
    }
}