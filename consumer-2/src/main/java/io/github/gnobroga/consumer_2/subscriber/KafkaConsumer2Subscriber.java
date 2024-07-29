package io.github.gnobroga.consumer_2.subscriber;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer2Subscriber {
    
    // Como já tem um consumer no mesmo groupId será feito o balanceamento de partições
    // @KafkaListener(topics = "topic-1", groupId = "group-1")
    // public void listen(String payload) {
    //     System.out.println("CONSUMER 2 RECEBEU: " + payload);
    // }

    //@Header(KafkaHeaders.RECEIVED_TOPIC)  Permite obter metadados do cabeçalho 
    // ConsumerRecordMetadata alternativa a annotation @Header
    @KafkaListener(topics = "topic-1", groupId = "group-2")
    public void listen(
        String payload, 
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        ConsumerRecordMetadata consumerRecordMetadata) {
        System.out.println("CONSUMER 2 RECEBEU: " + consumerRecordMetadata.offset());
    }
}
