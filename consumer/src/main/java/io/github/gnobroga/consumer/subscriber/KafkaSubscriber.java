package io.github.gnobroga.consumer.subscriber;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import io.github.gnobroga.consumer.model.City;
import io.github.gnobroga.consumer.model.Person;
import io.github.gnobroga.custom.PersonCustomListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KafkaSubscriber {
    
    //concurrency - Permite definir a quantidade de threads que podem ser abertas
    @KafkaListener(topics = "topic-1", groupId = "group-1", concurrency = "3") // o default containerFactory é <String, String>
    private void listen(List<String> batches) {
        System.out.println("CONSUMER 1: " + batches.size());
    }

    @PersonCustomListener
    private void listen(Person person) {
       System.out.println(person);
    }

    // // Ouvir algumas ou uma particição em específico.
    // @KafkaListener(topicPartitions = @TopicPartition(topic = "my-topic", partitions = "1-9" // "0-5" - intervalo
    // ))
    // public void listen(String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
    //     System.out.println("Patition: {} " + message);
    // }

    @KafkaListener(topics = "city-topic", groupId = "city-group", containerFactory = "jsonKafkaListenerContainerFactory")
    public void listenCities(List<City> cities) {
       cities.forEach(this::showCity);
    }

    private void showCity(City city) {
        System.out.println("Cidade: %s Estado: %s".formatted(city.getName(), city.getState()));
    }
}
