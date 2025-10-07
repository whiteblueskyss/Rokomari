package com.learn.rediss.controller;

import com.learn.rediss.model.VoteRequest;
import com.learn.rediss.service.PollService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/poll")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    // Vote for an option (JSON body)
    @PostMapping("/vote")
    public String vote(@RequestBody VoteRequest voteRequest) {
        String option = voteRequest.getOption();
        pollService.vote(option);
        return "Voted for option: " + option;
    }

    // Get vote count for a specific option (using URL params)
    @GetMapping("/vote/{option}")
    public String getVoteCount(@PathVariable String option) {
        return "Vote count for " + option + ": " + pollService.getVoteCount(option);
    }

    // Get the leaderboard (top N options)
    @GetMapping("/leaderboard")
    public Set<String> getLeaderboard(@RequestParam(defaultValue = "5") int topN) {
        return pollService.getLeaderboard(topN);
    }
}
