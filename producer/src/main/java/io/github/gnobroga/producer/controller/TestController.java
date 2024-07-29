package io.github.gnobroga.producer.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.gnobroga.producer.model.City;
import io.github.gnobroga.producer.model.Person;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
    
    // private final KafkaTemplate<String, String> kafkaTemplate;

    // private final KafkaTemplate<String, Serializable> jsonKafkaTemplate;

    private final RoutingKafkaTemplate routingKafkaTemplate;

    @GetMapping(value = "/send/city-topic", produces = { MediaType.APPLICATION_JSON_VALUE })
    public String sendToCityTopic() {
        final var c1 = new City("Castelo", "Espírito Santo");
        final var c2 = new City("Cachoeiro", "Espírito Santo");
        //jsonKafkaTemplate.send("city-topic", city);
        List.of(c1, c2).forEach(city -> routingKafkaTemplate.send("city-topic", city));
        return "{ \"sending\": true }";
    }


    @GetMapping(value = "/send/my-topic", produces = { MediaType.APPLICATION_JSON_VALUE })
    public String sendToMyTopic() {
        //kafkaTemplate.send("my-topic", "Sending for you!");
        routingKafkaTemplate.send("my-topic", "Sending For you");
        return "{ \"sending\": true }";
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(code = HttpStatus.OK)
    public String send() {
        IntStream.range(0, 100)
        .boxed()
        .forEach(this::sendData);
        return "{ \"sending\": true }";
    } 
 
    private void sendData(int data) { 
        //kafkaTemplate.send("topic-1", "Hello World! Number: " + data);
        routingKafkaTemplate.send("topic-1", "Hello World! Number: " + data);
    }

    @GetMapping(value = "/send/person", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(code = HttpStatus.OK)
    public String sendPerson() {
        //jsonKafkaTemplate.send("person-topic", Person.builder().name("Gabriel").age(24).build());
        routingKafkaTemplate.send("person-topic", Person.builder().name("Gabriel").age(24).build());
        return "{ \"sending\": true }";
    }
}
