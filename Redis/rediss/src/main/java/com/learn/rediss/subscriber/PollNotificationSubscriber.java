package com.learn.rediss.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PollNotificationSubscriber {

    private static final Logger log = LoggerFactory.getLogger(PollNotificationSubscriber.class);

    // This method is called when a message is published to the channel
    public void onMessage(String message, String channel) {
        log.info("Received message from channel [{}]: {}", channel, message);
        // You can add logic here to handle the message, such as sending real-time notifications to users
    }
}
