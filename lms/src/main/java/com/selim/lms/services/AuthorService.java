package com.selim.lms.services;

import com.selim.lms.dto.AuthorCreateUpdateDto;
import com.selim.lms.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto create(AuthorCreateUpdateDto dto);

    AuthorDto getById(Long id);
    
    List<AuthorDto> getAll();
    
    AuthorDto update(Long id, AuthorCreateUpdateDto dto);
    
    void delete(Long id);
}
