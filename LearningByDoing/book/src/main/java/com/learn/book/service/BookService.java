package com.learn.book.service;

import com.learn.book.model.Book;
import com.learn.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get a book by ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id);  // Directly return the Book object
    }

    // Save a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Update an existing book
    public Book updateBook(Long id, Book book) {
        Book existingBook = getBookById(id);
        if (existingBook != null) {
            book.setId(id);  // Ensures the book ID is preserved
            return bookRepository.save(book);
        }
        return null;  // If the book doesn't exist, return null
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
