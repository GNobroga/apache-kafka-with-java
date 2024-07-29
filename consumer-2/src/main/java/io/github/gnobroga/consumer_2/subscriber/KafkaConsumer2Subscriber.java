package io.github.gnobroga.consumer_2.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer2Subscriber {
    
    @KafkaListener(topics = "topic-1", groupId = "group-1")
    public void listen(String payload) {
        System.out.println("CONSUMER 2 RECEBEU: " + payload);
    }
}
