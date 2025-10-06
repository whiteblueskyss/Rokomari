package com.learn.rediss.config;

import org.springframework.context.annotation.Bean; // for Bean annotation. Which indicates that a method produces a bean to be managed by the Spring container.
import org.springframework.context.annotation.Configuration; // for Configuration annotation. Indicates that a class declares one or more @Bean methods and may be processed by the Spring container to generate bean definitions and service requests for those beans at runtime.
import org.springframework.data.redis.core.RedisTemplate; // RedisTemplate is the central class of Spring's Redis support that simplifies Redis data access code. It provides various operations for interacting with Redis, such as value operations, hash operations, list operations, set operations, and more.
import org.springframework.data.redis.core.StringRedisTemplate; // StringRedisTemplate is a specialized version of RedisTemplate that is designed to work specifically with String keys and String values. It simplifies the usage of Redis when the data being stored and retrieved is in String format.
import org.springframework.data.redis.connection.RedisConnectionFactory; // RedisConnectionFactory is an interface that provides a factory for creating connections to a Redis data store. It abstracts the creation and configuration of Redis connections, allowing for different implementations (e.g., standalone, clustered, or sentinel-based Redis setups).
import org.springframework.data.redis.serializer.StringRedisSerializer; // StringRedisSerializer is a serializer provided by Spring Data Redis that is used to convert String objects to and from their binary representation when storing and retrieving data in Redis. It ensures that String keys and values are properly serialized and deserialized when interacting with Redis.

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
