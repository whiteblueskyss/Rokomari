package com.selim.lms.services;

import com.selim.lms.dto.AuthorCreateUpdateDto;
import com.selim.lms.dto.AuthorDto;
import com.selim.lms.entities.Author;
import com.selim.lms.exceptions.NotFoundException;
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
        // ✅ Use the public constructor from Author that accepts a name
        Author author = new Author(dto.getName());
        Author saved = authorRepository.save(author);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));
        return toDto(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public AuthorDto update(Long id, AuthorCreateUpdateDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));
        author.setName(dto.getName());
        return toDto(author); // entity will be updated and flushed by JPA
    }

    @Override
    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NotFoundException("Author not found: " + id);
        }
        authorRepository.deleteById(id);
    }

    // --- mapping helper ---
    private AuthorDto toDto(Author a) {
        return new AuthorDto(a.getId(), a.getName());
    }
}
