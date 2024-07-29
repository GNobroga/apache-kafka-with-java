package io.github.gnobroga.consumer.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSubscriber {
    
    @KafkaListener(topics = "topic-1", groupId = "group-1")
    private void listen(String payload) {
        System.out.println("CONSUMER 1: " + payload);
    }
}
