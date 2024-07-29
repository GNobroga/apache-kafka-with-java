package io.github.gnobroga.producer.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PersonDLTSubscriber {
    
    // DLT são mensagens que tiveram erro ao serem enviadas.
    @KafkaListener(topics = "person-topic.DLT", groupId = "group-1")
    private void listenError(String payload) {
        log.info("Occurred an error when send person: {}", payload);
        System.out.println(payload);
    }
}
