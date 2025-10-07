package com.learn.rediss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.learn.rediss.subscriber.PollNotificationSubscriber;

@Configuration
public class RedisPubSubConfig {

    // Channel name for notifications
    public static final String CHANNEL = "poll-notifications";

    @Bean
    public RedisMessageListenerContainer listenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter messageListenerAdapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // Subscribe to the channel for poll notifications
        container.addMessageListener(messageListenerAdapter, new PatternTopic(CHANNEL));
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(PollNotificationSubscriber subscriber) {
        // Listen to the "onMessage" method on the subscriber
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}
