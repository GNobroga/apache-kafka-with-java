package io.github.gnobroga.producer.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class ProducerKafkaConfig {
    
    private final KafkaProperties kafkaProperties;

    @Bean  
    ProducerFactory<String, String> producerFactory() {
        final var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean  
    ProducerFactory<Object, Object> jsonProducerFactory() {
        final var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    ConsumerFactory<String, String> personConsumerFactory() {
        final var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    // Permite rotear para um tópico em tempo de execução dinamicamente com base em um pattern e um ProducerFactory.
    @Bean
    @SuppressWarnings("unchecked")
    RoutingKafkaTemplate routingKafkaTemplate(GenericApplicationContext context, ProducerFactory<Object, Object> jsonProducerFactory, ProducerFactory<String, String> producerFactory) {
        Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
        map.put(Pattern.compile("topic-.*"), (ProducerFactory<Object, Object>) (Object) producerFactory);
        map.put(Pattern.compile(".*-topic"), jsonProducerFactory);
        return new RoutingKafkaTemplate(map);
    }

    // @Bean
    // KafkaTemplate<String, Serializable> jsonKafkaTemplate() {
    //     return new KafkaTemplate<>(jsonProducerFactory());
    // }

    // @Bean
    // KafkaTemplate<String, String> kafkaTemplate() {
    //     return new KafkaTemplate<>(producerFactory());
    // }

    @Bean 
    KafkaAdmin kafkaAdmin() {
        final var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    } 

    // @Bean
    // NewTopic topic1() {
    //     // TopicBuilder - Alternativa
    //     return new NewTopic("topic-1", 2, (short) 1);
    // }

    @Bean
    KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
            TopicBuilder.name("topic-1").partitions(2).replicas(1).build(),
            TopicBuilder.name("my-topic").partitions(10).build(),
            TopicBuilder.name("city-topic").partitions(2).build(),
            TopicBuilder.name("person-topic").partitions(2).build(),
            TopicBuilder.name("person-topic.DLT").partitions(2).build()
        );
    }

}
