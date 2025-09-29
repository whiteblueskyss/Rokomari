package com.selim.lms.controllers;

import com.selim.lms.dto.UserCreateUpdateDto;
import com.selim.lms.dto.UserDto;
import com.selim.lms.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserCreateUpdateDto dto) {
        return userService.create(dto);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable @Positive(message = "User ID must be a positive number") Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable @Positive(message = "User ID must be a positive number") Long id, 
            @RequestBody @Valid UserCreateUpdateDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive(message = "User ID must be a positive number") Long id) {
        userService.delete(id);
    }
}
