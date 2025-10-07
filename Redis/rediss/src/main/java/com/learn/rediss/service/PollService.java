package com.learn.rediss.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class PollService {

    private final RedisTemplate<String, String> redisTemplate;

    public PollService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Increment the vote count for a specific poll option
    public void vote(String option) {
        // Add the vote to the Sorted Set (poll options are sorted by vote count)
        redisTemplate.opsForZSet().incrementScore("poll:leaderboard", option, 1);
    }

    // Get the vote count for a specific poll option
    public String getVoteCount(String option) {
        // Retrieve the current score (vote count) for the given option
        Double score = redisTemplate.opsForZSet().score("poll:leaderboard", option);
        return score != null ? score.toString() : "0";
    }

    // Get the leaderboard (top N options sorted by vote count)
    public Set<String> getLeaderboard(int topN) {
        // Get the top N options from the Sorted Set, ordered by score in descending order
        return redisTemplate.opsForZSet().reverseRange("poll:leaderboard", 0, topN - 1);
    }
}
