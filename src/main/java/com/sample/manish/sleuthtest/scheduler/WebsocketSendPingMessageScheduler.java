package com.sample.manish.sleuthtest.scheduler;

import com.sample.manish.sleuthtest.websocket.WebsocketAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.socket.PingMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This service is responsible to verify the server performance. It keeps sending the ping request and embed the timestamp.
 * Later handler receive the pong message and verify the time taken between ping and pong, to decide,
 * should we need to close the current under-performing connection so that a fresh new connection can be established
 */
@Service
@AllArgsConstructor
@Slf4j
public class WebsocketSendPingMessageScheduler {

    private final WebsocketAdapter websocketAdapter;

    @Scheduled(fixedDelayString = "5000")
    public void pingServerToTestCommunicationPerformance() {
        log.debug("ping message sent to the websocket server");
        if (websocketAdapter.isSessionActive()) {
            try {
                final PingMessage pingMessage = new PingMessage(buildPingMessageWithTimestamp());
                websocketAdapter.sendMessage(pingMessage);
                log.debug("ping message sent to the websocket server");
            } catch (IOException e) {
                log.error("exception occurred while closing the websocket connection. Manual intervention required!!!!. " +
                        "See below for detailed IO exception: {}", e);
            }
        }
    }

    private ByteBuffer buildPingMessageWithTimestamp() {
        final LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(5);
        return ByteBuffer.wrap(Objects.requireNonNull(SerializationUtils.serialize((localDateTime))));
    }
}