package com.learn.library.service;

import com.learn.library.model.Author;
import com.learn.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
