package io.github.gnobroga.producer.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(code = HttpStatus.OK)
    public String send() {
        kafkaTemplate.send("topic-1", "Sending message on time: %s".formatted(LocalDateTime.now().toString()));
        return "{ \"sending\": true }";
    }
}
