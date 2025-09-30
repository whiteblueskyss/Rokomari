package com.selim.lms.controllers;

import com.selim.lms.dto.AuthorCreateUpdateDto;
import com.selim.lms.dto.AuthorDto;
import com.selim.lms.services.AuthorService;
import com.selim.lms.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authors")
@Validated
public class AuthorController {

    private final AuthorService authorService;
    
    @Autowired
    private AuthUtil authUtil;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid AuthorCreateUpdateDto dto, HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Only admins can create authors"));
        }
        
        AuthorDto createdAuthor = authorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
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
    public ResponseEntity<?> update(
            @PathVariable @Positive(message = "Author ID must be a positive number") Long id, 
            @RequestBody @Valid AuthorCreateUpdateDto dto,
            HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Only admins can update authors"));
        }
        
        AuthorDto updatedAuthor = authorService.update(id, dto);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Positive(message = "Author ID must be a positive number") Long id, HttpServletRequest request) {
        if (!authUtil.isAuthenticated(request)) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Unauthorized - Please login first"));
        }
        
        if (!authUtil.isAdmin(request)) {
            return ResponseEntity.status(403)
                .body(Map.of("error", "Forbidden - Only admins can delete authors"));
        }
        
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
