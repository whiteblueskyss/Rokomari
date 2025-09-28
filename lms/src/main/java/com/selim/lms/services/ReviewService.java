package com.selim.lms.services;

import com.selim.lms.dto.ReviewCreateUpdateDto;
import com.selim.lms.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto create(ReviewCreateUpdateDto dto);
    ReviewDto getById(Long id);
    List<ReviewDto> getAll();
    List<ReviewDto> getByBook(Long bookId);
    List<ReviewDto> getByUser(Long userId);
    ReviewDto update(Long id, ReviewCreateUpdateDto dto);
    void delete(Long id);
}
