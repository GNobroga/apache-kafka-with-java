package io.github.gnobroga.producer.controller;

import java.io.Serializable;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.gnobroga.producer.model.Person;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaTemplate<String, Serializable> jsonKafkaTemplate;

    @GetMapping(value = "/send/my-topic", produces = { MediaType.APPLICATION_JSON_VALUE })
    public String sendToMyTopic() {
        kafkaTemplate.send("my-topic", "Sending for you!");
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
        kafkaTemplate.send("topic-1", "Hello World! Number: " + data);
    }

    @GetMapping(value = "/send/person", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(code = HttpStatus.OK)
    public String sendPerson() {
        jsonKafkaTemplate.send("person-topic", Person.builder().name("Gabriel").age(24).build());
        return "{ \"sending\": true }";
    }
}
