package com.learning.redis.repository;

import com.learning.redis.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class UserRepository {

    private final RedisTemplate<String, User> redisTemplate;

    @Autowired
    public UserRepository(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Save user to Redis with expiration time of 30 seconds
    public void saveUser(User user) {
        redisTemplate.opsForValue().set(user.getId(), user, Duration.ofSeconds(30));  // Expiration set to 30 seconds
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
