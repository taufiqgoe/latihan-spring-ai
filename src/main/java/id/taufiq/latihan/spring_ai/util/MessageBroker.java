package id.taufiq.latihan.spring_ai.util;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageBroker {

    private static final Logger log = LoggerFactory.getLogger(MessageBroker.class);
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageBroker(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String payload) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, payload);
            kafkaTemplate.send(record);
        } catch (Exception e) {
            log.error("Failed to send kafka message. topic={}", topic, e);
        }
    }
}
