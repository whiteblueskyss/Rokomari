package com.selim.lms.controllers;

import com.selim.lms.dto.BookCreateUpdateDto;
import com.selim.lms.dto.BookDto;
import com.selim.lms.services.BookService;
import com.selim.lms.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;
    
    @Autowired
    private AuthUtil authUtil;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid BookCreateUpdateDto dto, HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Only admins can create books"));
        }
        
        BookDto createdBook = bookService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable @Positive(message = "Book ID must be a positive number") Long id) {
        return bookService.getById(id);
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable @Positive(message = "Book ID must be a positive number") Long id, 
            @RequestBody @Valid BookCreateUpdateDto dto,
            HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Only admins can update books"));
        }
        
        BookDto updatedBook = bookService.update(id, dto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Positive(message = "Book ID must be a positive number") Long id, HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Only admins can delete books"));
        }
        
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get all books written by a specific author
    @GetMapping("/author/{authorId}")
    public List<BookDto> getBooksByAuthor(
            @PathVariable @Positive(message = "Author ID must be a positive number") Long authorId) {
        return bookService.getBooksByAuthor(authorId);
    }
}
