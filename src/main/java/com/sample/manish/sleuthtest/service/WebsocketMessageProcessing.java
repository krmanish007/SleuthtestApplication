package com.sample.manish.sleuthtest.service;

import com.sample.manish.sleuthtest.message.KafkaMessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WebsocketMessageProcessing {

    private final KafkaMessageSender<String> kafkaMessageSender;

    @NewSpan
    public void process(final String message) {
        log.info("websocket message - {}", message);

        kafkaMessageSender.send("kafkaTopic", message);
        log.info("message sent to {} with payload starting from {}", "kafkaTopic", message);
    }
}