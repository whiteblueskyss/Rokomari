package com.learn.rediss.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PollService {

    private final RedisTemplate<String, String> redisTemplate;

    public PollService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Increment the vote count for a specific poll option
    public void vote(String option) {
        redisTemplate.opsForValue().increment("poll:" + option, 1);  // Increment vote for option
    }

    // Get the vote count for a specific poll option
    public String getVoteCount(String option) {
        return redisTemplate.opsForValue().get("poll:" + option);  // Get current vote count for option
    }
}
