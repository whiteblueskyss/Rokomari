package com.learning.redis.controller;

import com.learning.redis.model.User;
import com.learning.redis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create user
    @PostMapping
    public String createUser(@RequestBody User user) {
        userRepository.saveUser(user);
        return "User created successfully!";
    }

    // Retrieve user by ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userRepository.getUserById(id);
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        userRepository.deleteUser(id);
        return "User deleted successfully!";
    }
}
