package com.selim.lms.exceptions;

/**
 * Exception thrown when invalid data is provided in requests
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}