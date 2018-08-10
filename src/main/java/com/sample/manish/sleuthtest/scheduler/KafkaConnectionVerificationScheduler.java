package com.sample.manish.sleuthtest.scheduler;

import com.sample.manish.sleuthtest.message.KafkaAdminClient;
import com.sample.manish.sleuthtest.websocket.WebsocketAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

/**
 * This service is responsible to verify the server performance. It keeps sending the ping request and embed the timestamp.
 * Later handler receive the pong message and verify the time taken between ping and pong, to decide,
 * should we need to close the current under-performing connection so that a fresh new connection can be established
 */
//@Service
@AllArgsConstructor
@Slf4j
public class KafkaConnectionVerificationScheduler {

    private KafkaAdminClient kafkaAdminClient;
    private WebsocketAdapter websocketAdapter;
    private static boolean serverStarted = false;

    @Getter
    private static boolean lastKnownKafkaServerStatusActive = true;

    @Scheduled(initialDelayString = "5000",
            fixedDelayString = "5000")
    public void verifyIfKafkaServerIsRunning() throws IOException {
        log.debug("ping message sent to the websocket server");

        if (!serverStarted) {
            return;
        }

        boolean kafkaBrokerStatus = kafkaAdminClient.isKafkaBrokerRunning();

        if (isKafkaBrokerGoneDownNow(kafkaBrokerStatus)) {
            //Kafka server has gone down, so flag it and stop all request to start new connection
            lastKnownKafkaServerStatusActive = false;
            websocketAdapter.closeSession();
        }
        else if (isKafkaBrokerStartedRunningSuccessfully(kafkaBrokerStatus)) {
            //kafka server was down but now came back, so start a new connection
            lastKnownKafkaServerStatusActive = true;
            websocketAdapter.startNewConnection();
        }
    }

    private boolean isKafkaBrokerStartedRunningSuccessfully(boolean kafkaBrokerStatus) {
        return !lastKnownKafkaServerStatusActive && kafkaBrokerStatus;
    }

    private boolean isKafkaBrokerGoneDownNow(boolean kafkaBrokerStatus) {
        return lastKnownKafkaServerStatusActive && !kafkaBrokerStatus;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onServerStart() {
        serverStarted = true;
    }
}
