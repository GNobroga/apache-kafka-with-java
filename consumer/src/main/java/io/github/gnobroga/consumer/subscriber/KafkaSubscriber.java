package io.github.gnobroga.consumer.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaSubscriber {
    
    @KafkaListener(topics = "topic-1", groupId = "group-1")
    private void listen(String payload) {
        log.info("Entered message: {}", payload);
        System.out.println(payload);
    }
}
