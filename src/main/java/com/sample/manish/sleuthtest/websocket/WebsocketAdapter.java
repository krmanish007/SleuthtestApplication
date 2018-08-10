package com.sample.manish.sleuthtest.websocket;

import io.micrometer.core.instrument.Counter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * This class purely deals with websocket along with web socket handler, and expose methods for others to operate retry and other operations
 */
@Service
@Slf4j
@AllArgsConstructor
public class WebsocketAdapter {

    private static WebSocketSession webSocketSession = null;

    private Counter websocketNewConnectionRequestCounter;

    private WebSocketHandler webSocketHandler;

    public void startNewConnection() {
            websocketNewConnectionRequestCounter.increment();

            try {
                webSocketSession = new StandardWebSocketClient().doHandshake(
                        webSocketHandler,
                        new WebSocketHttpHeaders(),
                        new URI("ws://localhost:9998/testwebsocket"))
                        .get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("exception occurred while opening the websocket connection. Manual intervention required!!!!. " +
                        "See below for detailed IO exception", e);
            }
            log.info("new websocket connection established");
    }

    /**
     * This will start the new connection after server start
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onServerStart() {
        startNewConnection();
    }

    public boolean isSessionActive() {
        return webSocketSession != null && webSocketSession.isOpen();
    }

    public void sendMessage(final WebSocketMessage<?> message) throws IOException {
        webSocketSession.sendMessage(message);
    }

    public void closeSession() throws IOException {
        if (webSocketSession != null) {
            webSocketSession.close();
        }
    }
}
