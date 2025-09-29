package com.selim.lms.controllers;

import com.selim.lms.dto.AuthorCreateUpdateDto;
import com.selim.lms.dto.AuthorDto;
import com.selim.lms.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Validated
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@RequestBody @Valid AuthorCreateUpdateDto dto) {
        return authorService.create(dto);
    }

    @GetMapping("/{id}")
    public AuthorDto getById(@PathVariable @Positive(message = "Author ID must be a positive number") Long id) {
        return authorService.getById(id);
    }

    @GetMapping
    public List<AuthorDto> getAll() {
        return authorService.getAll();
    }

    @PutMapping("/{id}")
    public AuthorDto update(
            @PathVariable @Positive(message = "Author ID must be a positive number") Long id, 
            @RequestBody @Valid AuthorCreateUpdateDto dto) {
        return authorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = "Author ID must be a positive number") Long id) {
        authorService.delete(id);
    }
}
