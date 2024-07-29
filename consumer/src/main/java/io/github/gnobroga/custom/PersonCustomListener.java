package io.github.gnobroga.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@KafkaListener
public @interface PersonCustomListener {
    
    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "group-1";

    @AliasFor(annotation = KafkaListener.class, attribute = "topics") 
    String[] topics() default "person-topic";

    @AliasFor(annotation = KafkaListener.class, attribute = "containerFactory")
    String containerFactory() default "jsonKafkaListenerContainerFactory";
};
