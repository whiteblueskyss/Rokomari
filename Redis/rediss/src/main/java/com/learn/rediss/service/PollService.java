package com.learn.rediss.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Set;

@Service
public class PollService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public PollService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Increment the vote count for a specific poll option
    public void vote(String option) {
        redisTemplate.opsForZSet().incrementScore("poll:leaderboard", option, 1); // Increment vote for option
        
        // After incrementing the vote count, check if the threshold is reached
        Double score = redisTemplate.opsForZSet().score("poll:leaderboard", option);
        if (score != null && score >= 5) {
            // Publish a notification when the vote threshold is reached (5 votes)
            String message = "Option '" + option + "' has reached " + score.intValue() + " votes!";
            redisTemplate.convertAndSend("poll-notifications", message);
        }
    }

    // Get the vote count for a specific poll option
    public String getVoteCount(String option) {
        Double score = redisTemplate.opsForZSet().score("poll:leaderboard", option);
        return score != null ? score.toString() : "0";
    }

    // Get the leaderboard (top N options)
    public Set<String> getLeaderboard(int topN) {
        return redisTemplate.opsForZSet().reverseRange("poll:leaderboard", 0, topN - 1);
    }
}
