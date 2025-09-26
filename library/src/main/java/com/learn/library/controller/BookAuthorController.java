package com.learn.library.controller;

import com.learn.library.model.BookAuthorDTO;
import com.learn.library.service.BookAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-author")
public class BookAuthorController {

    @Autowired
    private BookAuthorService bookAuthorService;

    // Assign an author to a book
    @PostMapping("/assign")
    public ResponseEntity<Void> assignAuthorToBook(@RequestBody BookAuthorDTO dto) {
        bookAuthorService.assignAuthorToBook(dto);
        return ResponseEntity.status(201).build();
    }

    // Remove an author from a book
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeAuthorFromBook(@RequestBody BookAuthorDTO dto) {
        bookAuthorService.removeAuthorFromBook(dto);
        return ResponseEntity.noContent().build();
    }

    // Get all authors for a book
    @GetMapping("/book/{bookId}")
    public List<Long> getAuthorsByBook(@PathVariable Long bookId) {
        return bookAuthorService.getAuthorsByBook(bookId);
    }

    // Get all books for an author
    @GetMapping("/author/{authorId}")
    public List<Long> getBooksByAuthor(@PathVariable Long authorId) {
        return bookAuthorService.getBooksByAuthor(authorId);
    }
}
