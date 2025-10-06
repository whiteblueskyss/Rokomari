package com.learn.rediss.controller;

import com.learn.rediss.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/save")
    public String saveToRedis(@RequestParam String key, @RequestParam String value) {
        redisService.saveValue(key, value);
        return "Value saved!";
    }

    @GetMapping("/get")
    public String getFromRedis(@RequestParam String key) {
        return redisService.getValue(key);
    }
}
