package com.learn.library.service;

import com.learn.library.model.Author;
import com.learn.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Service is a Spring annotation that indicates that the class is a "Service", which is a specialization of the @Component annotation. It is used to define business logic and service layer components. Spring creates a bean for this class and controls its lifecycle.

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    // Get all authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Get an author by ID
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    // Create a new author
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    // Update an author
    public Author updateAuthor(Author author) {
        return authorRepository.update(author);
    }

    // Delete an author by ID
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}
