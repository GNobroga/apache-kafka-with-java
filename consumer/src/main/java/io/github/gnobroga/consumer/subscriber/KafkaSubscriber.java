package io.github.gnobroga.consumer.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import io.github.gnobroga.consumer.model.Person;
import io.github.gnobroga.custom.PersonCustomListener;

@Component
public class KafkaSubscriber {
    
    // concurrency - Permite definir a quantidade de threads que podem ser abertas
    // @KafkaListener(topics = "topic-1", groupId = "group-1", concurrency = "3") // o default containerFactory é <String, String>
    // private void listen(String payload) {
    //     System.out.println("CONSUMER 1: " + payload);
    // }

    // @PersonCustomListener
    // private void listen(Person person) {
    //    System.out.println(person);
    // }

    // // Ouvir algumas ou uma particição em específico.
    // @KafkaListener(topicPartitions = @TopicPartition(topic = "my-topic", partitions = "1-9" // "0-5" - intervalo
    // ))
    // public void listen(String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
    //     System.out.println("Patition: {} " + message);
    // }
}
