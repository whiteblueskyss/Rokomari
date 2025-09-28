package com.selim.lms.controllers;

import com.selim.lms.dto.ReviewCreateUpdateDto;
import com.selim.lms.dto.ReviewDto;
import com.selim.lms.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto create(@RequestBody @Valid ReviewCreateUpdateDto dto) {
        return reviewService.create(dto);
    }

    @GetMapping("/{id}")
    public ReviewDto getById(@PathVariable Long id) {
        return reviewService.getById(id);
    }

    @GetMapping
    public List<ReviewDto> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/book/{bookId}")
    public List<ReviewDto> getByBook(@PathVariable Long bookId) {
        return reviewService.getByBook(bookId);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewDto> getByUser(@PathVariable Long userId) {
        return reviewService.getByUser(userId);
    }

    @PutMapping("/{id}")
    public ReviewDto update(@PathVariable Long id, @RequestBody @Valid ReviewCreateUpdateDto dto) {
        return reviewService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
