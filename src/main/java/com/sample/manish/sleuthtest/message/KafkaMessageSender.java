package com.sample.manish.sleuthtest.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaMessageSender<T> {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper mapper;

    public void send(final String topicName, T payload) {
        try {
            kafkaTemplate.send(topicName, mapper.writeValueAsString(payload).getBytes());
        } catch (JsonProcessingException e) {
            log.error("error sending payload to topic='{}'", topicName);
        }
        log.info("sent payload to topic='{}'", topicName);
    }
}
