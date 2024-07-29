package io.github.gnobroga.producer.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Person implements Serializable {
    
    private String name;

    private Integer age;
}
