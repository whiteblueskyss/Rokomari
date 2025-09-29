package com.selim.lms.services;

import com.selim.lms.dto.AuthorCreateUpdateDto;
import com.selim.lms.dto.AuthorDto;
import com.selim.lms.entities.Author;
import com.selim.lms.exceptions.NotFoundException;
import com.selim.lms.exceptions.DuplicateResourceException;
import com.selim.lms.exceptions.BusinessValidationException;
import com.selim.lms.exceptions.InvalidDataException;
import com.selim.lms.repos.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto create(AuthorCreateUpdateDto dto) {
        validateAuthorCreateUpdateDto(dto);
        
        // Check for duplicate author name (business rule: author names should be unique)
        if (authorRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Author with name '" + dto.getName() + "' already exists");
        }

        Author author = new Author(dto.getName());
        Author saved = authorRepository.save(author);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getById(Long id) {
        validateId(id, "Author ID");
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + id));
        return mapToDto(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public AuthorDto update(Long id, AuthorCreateUpdateDto dto) {
        validateId(id, "Author ID");
        validateAuthorCreateUpdateDto(dto);
        
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + id));

        // Check for duplicate author name (excluding current author)
        if (!author.getName().equals(dto.getName()) && authorRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Author with name '" + dto.getName() + "' already exists");
        }

        author.setName(dto.getName());
        return mapToDto(author);
    }

    @Override
    public void delete(Long id) {
        validateId(id, "Author ID");
        
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with ID: " + id));

        // Business rule: Check if author has books associated
        if (!author.getBooks().isEmpty()) {
            throw new BusinessValidationException(
                "Cannot delete author '" + author.getName() + "' because they have " + 
                author.getBooks().size() + " book(s) associated. Please remove the author from all books first."
            );
        }
        
        authorRepository.deleteById(id);
    }

    // Private validation methods
    private void validateAuthorCreateUpdateDto(AuthorCreateUpdateDto dto) {
        if (dto == null) {
            throw new InvalidDataException("Author data cannot be null");
        }
        
        if (dto.getName() != null && dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("Author name cannot be empty or contain only whitespace");
        }
    }

    private void validateId(Long id, String fieldName) {
        if (id == null) {
            throw new InvalidDataException(fieldName + " cannot be null");
        }
        if (id <= 0) {
            throw new InvalidDataException(fieldName + " must be a positive number");
        }
    }

    private AuthorDto mapToDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
