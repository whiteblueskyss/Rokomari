package com.selim.lms.services;

import com.selim.lms.dto.BookCreateUpdateDto;
import com.selim.lms.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto create(BookCreateUpdateDto dto);
    BookDto getById(Long id);
    List<BookDto> getAll();
    BookDto update(Long id, BookCreateUpdateDto dto);
    void delete(Long id);

    List<BookDto> getBooksByAuthor(Long authorId);

    // Optional helper if you want a dedicated endpoint to replace authors
    BookDto replaceAuthors(Long bookId, List<Long> authorIds);
}
