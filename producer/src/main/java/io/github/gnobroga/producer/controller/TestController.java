package io.github.gnobroga.producer.controller;

import java.time.LocalDateTime;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        IntStream.range(0, 100)
            .boxed()
            .forEach(n -> kafkaTemplate.send("topic-1", "You receive a number: " + n));
        return "{ \"sending\": true }";
    }
}
