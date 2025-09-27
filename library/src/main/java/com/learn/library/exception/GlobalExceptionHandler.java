package com.learn.library.exception;

import org.springframework.http.HttpStatus; // HttpStatus is an enumeration in Spring Framework that represents HTTP status codes. It provides a convenient way to work with standard HTTP status codes in your application.
import org.springframework.http.ResponseEntity; // ResponseEntity is a class in Spring Framework that represents an HTTP response, including status code, headers, and body. It is used to send responses from RESTful web services. 
import org.springframework.web.bind.MethodArgumentNotValidException; // MethodArgumentNotValidException is an exception thrown when validation on an argument annotated with @Valid fails. It is commonly used in Spring MVC applications to handle validation errors for request bodies.
import org.springframework.web.bind.annotation.ExceptionHandler; // ExceptionHandler is an annotation in Spring Framework that is used to define a method as an exception handler. It allows you to specify which exceptions the method should handle and how to handle them, providing a way to centralize exception handling logic in your application.
import org.springframework.web.bind.annotation.ControllerAdvice; // ControllerAdvice is an annotation in Spring Framework that allows you to define global exception handling and other cross-cutting concerns for multiple controllers. It is used to create a centralized place for handling exceptions, applying common logic, and customizing responses across different controller classes.

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle BookNotFoundException
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBookNotFoundException(BookNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleAuthorNotFoundException(AuthorNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle InvalidReviewException
    @ExceptionHandler(InvalidReviewException.class)
    public ResponseEntity<Map<String, String>> handleInvalidReviewException(InvalidReviewException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle validation errors (from @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
