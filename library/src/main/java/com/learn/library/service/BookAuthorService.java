package com.learn.library.service;

import com.learn.library.model.BookAuthorDTO;
import com.learn.library.repository.BookAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookAuthorService {

    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    // Assign an author to a book
    public void assignAuthorToBook(BookAuthorDTO dto) {
        bookAuthorRepository.assignAuthorToBook(dto.getBookId(), dto.getAuthorId());
    }

    // Remove an author from a book
    public void removeAuthorFromBook(BookAuthorDTO dto) {
        bookAuthorRepository.removeAuthorFromBook(dto.getBookId(), dto.getAuthorId());
    }

    // Get all authors for a book
    public List<Long> getAuthorsByBook(Long bookId) {
        return bookAuthorRepository.getAuthorsByBook(bookId);
    }

    // Get all books for an author
    public List<Long> getBooksByAuthor(Long authorId) {
        return bookAuthorRepository.getBooksByAuthor(authorId);
    }
}
