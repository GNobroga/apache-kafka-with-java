package io.github.gnobroga.consumer.config;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import io.github.gnobroga.consumer.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EnableKafka
@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerConfig {
    
    private final KafkaProperties kafkaProperties;

    @Bean
    ConsumerFactory<String, String> consumerFactory() {
        final var config = new HashMap<String, Object>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean // Permite definir configurações para o Container de Listener
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(2); // Setando um concurrency default para o containerFactory do KafkaListener default.
        factory.setBatchListener(true); // Permite a leitura em lotes, ou seja, se houver muitas mensagens enviadas ler tudo de uma vez só. O conteúdo será colocado dentro de uma Lista.
        return factory;
    }


    @Bean
    @SuppressWarnings("resource")
    ConsumerFactory<String, Person> personConsumerFactory() {
        final var config = new HashMap<String, Object>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        final var jsonDeserializer = new JsonDeserializer<>(Person.class)
            .trustedPackages("*") // Confia em qualquer pacote
            .forKeys();
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }


    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Person> personKafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Person>();
        factory.setConsumerFactory(personConsumerFactory());
        //factory.setRecordInterceptor(adultInterceptor());
        //factory.setRecordInterceptor(exampleInterceptor());
        // factory.setBatchInterceptor(null); Interceptador para Batch

        factory.setCommonErrorHandler(defaultErrorHandler());
        return factory;
    }
 

    private DefaultErrorHandler defaultErrorHandler() {
        // DeadLetter vai tentar enviar para um topic igual ao que está sendo direcionado, mas com person-topic.DLT
        var recoverer = new DeadLetterPublishingRecoverer(new KafkaTemplate<>(personProducerFactory()), (consumerRecord, exception) -> {

            return new TopicPartition(consumerRecord.topic() + ".DLT", 1); // Alterando o padrão de envio da DLT.
        });
        // Permite definir um Error Handler com números de tentativas.
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000, 0));
    }

    @Bean  
    ProducerFactory<String, Person> personProducerFactory() {
        final var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
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


    private RecordInterceptor<String, Person> exampleInterceptor() {
        return new RecordInterceptor<>() {
            
            @Override
            public ConsumerRecord<String, Person> intercept(ConsumerRecord<String, Person> record, Consumer<String, Person> consumer) {
                return record;
            }

            @Override // Quando a passar com sucesso pelo interceptador.
            public void success(ConsumerRecord<String, Person> record, Consumer<String, Person> consumer) {
                log.info("Person intercepted with success");
            }

            @Override // Quando for estourado uma execeção no interceptador
            public void failure(ConsumerRecord<String, Person> record, Exception exception,
                    Consumer<String, Person> consumer) {
                log.info("Ocurred an error when passing for example interceptor.");
            }
        };
    }

    // Interceptor, it run before listener.
    public RecordInterceptor<String, Person> adultInterceptor() {
        return (record, consumer) -> {
            log.info("Person intercept with {}", record);
            final var person = record.value();
            if (person != null && person.getAge() != null && person.getAge() < 18) {
                return null; // Não irá para o Listener.
            }
            return record;
        };
    }


    @Bean
    ConsumerFactory<String, String> jsonConsumerFactory() {
        final var config = new HashMap<String, Object>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean // Permite definir configurações KafkaListener
    ConcurrentKafkaListenerContainerFactory<String, String> jsonKafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(jsonConsumerFactory());
        factory.setConcurrency(2); 
        //factory.setRecordMessageConverter(new JsonMessageConverter()); // Tenta deserializar para qualquer tipo de objeto
        
        // Permite a leitura em lote de objetos, serão colocados dentro de uma Lista.
        factory.setBatchMessageConverter(new BatchMessagingMessageConverter(new JsonMessageConverter()));
        factory.setBatchListener(true);
        return factory;
    }


}
