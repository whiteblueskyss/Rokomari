package com.learning.redis.repository;

import com.learning.redis.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

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

      // Add user to Redis List (e.g., store all users created today)
    public void addUserToList(User user) {
        redisTemplate.opsForList().rightPush("userList", user);  // Add user to the end of the list
    }

    // Retrieve all users from the Redis List
    public List<User> getAllUsersFromList() {
        return redisTemplate.opsForList().range("userList", 0, -1);  // Retrieve all users in the list
    }
}
