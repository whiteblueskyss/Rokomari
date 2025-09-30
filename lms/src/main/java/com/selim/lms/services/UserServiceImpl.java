package com.selim.lms.services;

import com.selim.lms.dto.UserCreateUpdateDto;
import com.selim.lms.dto.UserDto;
import com.selim.lms.entities.Role;
import com.selim.lms.entities.User;
import com.selim.lms.exceptions.NotFoundException;
import com.selim.lms.exceptions.DuplicateResourceException;
import com.selim.lms.exceptions.BusinessValidationException;
import com.selim.lms.exceptions.InvalidDataException;
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
        validateUserCreateUpdateDto(dto);
        
        // Check for duplicate email
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User with email '" + dto.getEmail() + "' already exists");
        }

        // Validate and parse role
        Role role;
        try {
            role = Role.valueOf(dto.getRole());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Invalid role: '" + dto.getRole() + "'. Valid roles are: READER, ADMIN");
        }

        User user = new User(dto.getName(), dto.getEmail(), role);
        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        validateId(id, "User ID");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public UserDto update(Long id, UserCreateUpdateDto dto) {
        validateId(id, "User ID");
        validateUserCreateUpdateDto(dto);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));

        // Check for duplicate email (excluding current user)
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("User with email '" + dto.getEmail() + "' already exists");
        }

        // Validate and parse role
        Role role;
        try {
            role = Role.valueOf(dto.getRole());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Invalid role: '" + dto.getRole() + "'. Valid roles are: READER, ADMIN");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(role);

        return mapToDto(user);
    }

    @Override
    public void delete(Long id) {
        validateId(id, "User ID");
        
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with ID: " + id);
        }
        
        // Business rule: Could add check if user has reviews that need to be handled
        userRepository.deleteById(id);
    }

    // Private validation methods
    private void validateUserCreateUpdateDto(UserCreateUpdateDto dto) {
        if (dto == null) {
            throw new InvalidDataException("User data cannot be null");
        }
        
        if (dto.getName() != null && dto.getName().trim().isEmpty()) {
            throw new InvalidDataException("User name cannot be empty or contain only whitespace");
        }
        
        if (dto.getEmail() != null && dto.getEmail().trim().isEmpty()) {
            throw new InvalidDataException("User email cannot be empty or contain only whitespace");
        }
        
        if (dto.getRole() != null && dto.getRole().trim().isEmpty()) {
            throw new InvalidDataException("User role cannot be empty or contain only whitespace");
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

    private UserDto mapToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

     @Override
    public boolean authenticateUserById(Long userId) {
        // Check if the user exists by user ID
        User user = userRepository.findById(userId).orElse(null);
        return user != null;  // If the user exists, return true
    }

}
