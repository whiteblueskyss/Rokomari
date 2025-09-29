package com.selim.lms.controllers;

import com.selim.lms.dto.BookCreateUpdateDto;
import com.selim.lms.dto.BookDto;
import com.selim.lms.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid BookCreateUpdateDto dto) {
        return bookService.create(dto);
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
    public BookDto update(
            @PathVariable @Positive(message = "Book ID must be a positive number") Long id, 
            @RequestBody @Valid BookCreateUpdateDto dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = "Book ID must be a positive number") Long id) {
        bookService.delete(id);
    }

    // Endpoint to get all books written by a specific author
    @GetMapping("/author/{authorId}")
    public List<BookDto> getBooksByAuthor(
            @PathVariable @Positive(message = "Author ID must be a positive number") Long authorId) {
        return bookService.getBooksByAuthor(authorId);
    }
}
