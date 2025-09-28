package com.selim.lms.services;

import com.selim.lms.dto.UserCreateUpdateDto;
import com.selim.lms.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserCreateUpdateDto dto);
    UserDto getById(Long id);
    List<UserDto> getAll();
    UserDto update(Long id, UserCreateUpdateDto dto);
    void delete(Long id);
}
