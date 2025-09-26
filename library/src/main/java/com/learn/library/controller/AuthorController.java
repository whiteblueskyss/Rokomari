package com.learn.library.controller;

import com.learn.library.model.Author;
import com.learn.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // ResponseEntity represents the whole HTTP response: status code, headers, and body. It allows you to fully configure the HTTP response.
import org.springframework.web.bind.annotation.*; // This package contains annotations for mapping web requests to specific handler classes and methods. like @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PathVariable, and @RequestBody.

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // Get all authors
    @GetMapping
    public List<Author> getAllAuthors() { // it always returns a list, even if empty so don't need ResponseEntity. ResponseEntity is useful when you want to return different status codes or custom headers. 
        return authorService.getAllAuthors();
    }

    // Get an author by ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        if (author != null) {
            return ResponseEntity.ok(author);
        }
        return ResponseEntity.notFound().build();
    }

    // Create a new author
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(201).body(savedAuthor);
    }

    // Update an author
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        author.setId(id); // Ensure ID is set
        Author updatedAuthor = authorService.updateAuthor(author);
        if (updatedAuthor != null) {
            return ResponseEntity.ok(updatedAuthor);
        }
        return ResponseEntity.notFound().build(); //Why use .build()? Sometimes you want a response with no body (just status + headers). .build() means: “I’m done, send this response as-is.” sometimes you want to return a response with no body, just status code and headers.
    }

    // Delete an author
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
