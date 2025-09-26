package com.learn.library.service;

import com.learn.library.exception.BookNotFoundException;
import com.learn.library.exception.AuthorNotFoundException;
import com.learn.library.model.BookAuthorDTO;
import com.learn.library.repository.AuthorRepository;
import com.learn.library.repository.BookAuthorRepository;
import com.learn.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.learn.library.exception.*;
import com.learn.library.model.*;
import com.learn.library.repository.*;


import java.util.List;

@Service
public class BookAuthorService {

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    // Assign an author to a book
    public void assignAuthorToBook(BookAuthorDTO dto) {
        if (bookRepository.findById(dto.getBookId()) == null) {
            throw new BookNotFoundException(dto.getBookId());
        }
        if (authorRepository.findById(dto.getAuthorId()) == null) {
            throw new AuthorNotFoundException(dto.getAuthorId());
        }
        bookAuthorRepository.assignAuthorToBook(dto.getBookId(), dto.getAuthorId());
    }

    // Remove an author from a book
    public void removeAuthorFromBook(BookAuthorDTO dto) {
        if (bookRepository.findById(dto.getBookId()) == null) {
            throw new BookNotFoundException(dto.getBookId());
        }
        if (authorRepository.findById(dto.getAuthorId()) == null) {
            throw new AuthorNotFoundException(dto.getAuthorId());
        }
        bookAuthorRepository.removeAuthorFromBook(dto.getBookId(), dto.getAuthorId());
    }

    // Get all authors for a book
    public List<Long> getAuthorsByBook(Long bookId) {
        if (bookRepository.findById(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        return bookAuthorRepository.getAuthorsByBook(bookId);
    }

    // Get all books for an author
    public List<Long> getBooksByAuthor(Long authorId) {
        if (authorRepository.findById(authorId) == null) {
            throw new AuthorNotFoundException(authorId);
        }
        return bookAuthorRepository.getBooksByAuthor(authorId);
    }
}
