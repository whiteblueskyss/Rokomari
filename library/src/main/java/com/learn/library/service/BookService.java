package com.learn.library.service;

import com.learn.library.model.Book;
import com.learn.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.learn.library.exception.BookNotFoundException;

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
        Book existingBook = bookRepository.findById(id);
        if (existingBook == null) {
            throw new BookNotFoundException(id);
        }
        return existingBook;
    }

    // Create a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Update a book
    public Book updateBook(Book book) {
        Book existingBook = bookRepository.findById(book.getId());
        if (existingBook == null) {
            throw new BookNotFoundException(book.getId());
        }
        return bookRepository.update(book); // now safe
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id);
        if (existingBook == null) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id); // now safe
    }

}
