package com.learning.redis.repository;

import com.learning.redis.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final RedisTemplate<String, User> redisTemplate;

    @Autowired
    public UserRepository(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Save user to Redis
    public void saveUser(User user) {
        redisTemplate.opsForValue().set(user.getId(), user);  // Storing user with id as key
    }

    // Retrieve user by ID from Redis
    public User getUserById(String id) {
        return redisTemplate.opsForValue().get(id);  // Retrieving user using the ID
    }

    // Delete user by ID from Redis
    public void deleteUser(String id) {
        redisTemplate.delete(id);  // Deleting the user with the provided ID
    }
}
