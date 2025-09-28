package com.selim.lms.services;

import com.selim.lms.dto.AuthorBriefDto;
import com.selim.lms.dto.BookCreateUpdateDto;
import com.selim.lms.dto.BookDto;
import com.selim.lms.entities.Author;
import com.selim.lms.entities.Book;
import com.selim.lms.exceptions.NotFoundException;
import com.selim.lms.repos.AuthorRepository;
import com.selim.lms.repos.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public BookDto create(BookCreateUpdateDto dto) {
        Book book = new Book(dto.getTitle(), dto.getGenre(), dto.getPublishDate());

        // Attach authors if provided
        Set<Author> authors = fetchAndValidateAuthors(dto.getAuthorIds());
        if (!authors.isEmpty()) {
            book.setAuthors(authors);
            // keep both sides consistent
            authors.forEach(a -> a.getBooks().add(book));
        }

        Book saved = bookRepository.save(book);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        return toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BookDto update(Long id, BookCreateUpdateDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        book.setTitle(dto.getTitle());
        book.setGenre(dto.getGenre());
        book.setPublishDate(dto.getPublishDate());

        if (dto.getAuthorIds() != null) {
            // replace authors set
            Set<Author> authors = fetchAndValidateAuthors(dto.getAuthorIds());

            // clear old links from both sides
            book.getAuthors().forEach(a -> a.getBooks().remove(book));
            book.getAuthors().clear();

            // set new links
            book.setAuthors(authors);
            authors.forEach(a -> a.getBooks().add(book));
        }

        return toDto(book); // managed entity will flush on tx commit
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto replaceAuthors(Long bookId, List<Long> authorIds) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        Set<Author> authors = fetchAndValidateAuthors(authorIds);

        // clear old and set new (keeping both sides consistent)
        book.getAuthors().forEach(a -> a.getBooks().remove(book));
        book.getAuthors().clear();

        book.setAuthors(authors);
        authors.forEach(a -> a.getBooks().add(book));

        return toDto(book);
    }

    // ---------- helpers ----------

    private Set<Author> fetchAndValidateAuthors(List<Long> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<Author> found = authorRepository.findAllById(authorIds);
        if (found.size() != new HashSet<>(authorIds).size()) {
            // figure out which ones are missing
            Set<Long> foundIds = found.stream().map(Author::getId).collect(Collectors.toSet());
            List<Long> missing = authorIds.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new NotFoundException("Author(s) not found: " + missing);
        }
        return new LinkedHashSet<>(found);
    }

    private BookDto toDto(Book b) {
        List<AuthorBriefDto> authorDtos = b.getAuthors()
                .stream()
                .map(a -> new AuthorBriefDto(a.getId(), a.getName()))
                .toList();
        return new BookDto(b.getId(), b.getTitle(), b.getGenre(), b.getPublishDate(), authorDtos);
    }

 // Method to get all books by an author
    @Override
    public List<BookDto> getBooksByAuthor(Long authorId) {
        // Fetch author by ID
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found: " + authorId));

        // Return books written by the author as DTOs
        return author.getBooks().stream()
                .map(this::toDto)
                .toList();
    }
}
