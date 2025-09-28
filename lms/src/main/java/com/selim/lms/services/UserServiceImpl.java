package com.selim.lms.services;

import com.selim.lms.dto.UserCreateUpdateDto;
import com.selim.lms.dto.UserDto;
import com.selim.lms.entities.Role;
import com.selim.lms.entities.User;
import com.selim.lms.exceptions.NotFoundException;
import com.selim.lms.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserCreateUpdateDto dto) {
        User user = new User(dto.getName(), dto.getEmail(), Role.valueOf(dto.getRole()));
        User saved = userRepository.save(user);
        return new UserDto(saved.getId(), saved.getName(), saved.getEmail(), saved.getRole().name());
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name()))
                .toList();
    }

    @Override
    public UserDto update(Long id, UserCreateUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.valueOf(dto.getRole()));

        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
