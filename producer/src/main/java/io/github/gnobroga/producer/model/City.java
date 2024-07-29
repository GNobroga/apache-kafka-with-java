package io.github.gnobroga.producer.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class City implements Serializable {
    
    private String name;

    private String state;
}
