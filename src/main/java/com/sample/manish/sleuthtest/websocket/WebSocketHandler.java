package com.sample.manish.sleuthtest.websocket;

import com.sample.manish.sleuthtest.service.WebsocketMessageProcessing;
import io.micrometer.core.instrument.Counter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Service
@AllArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private static StringBuilder betSyncMessage = new StringBuilder();

    private final WebsocketMessageProcessing websocketMessageProcessing;

    @Qualifier("messageReceivedFromBetsyncCounter")
    private Counter messageReceivedFromBetsyncCounter;

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) {
        betSyncMessage.append(message.getPayload());
        if (message.isLast()) {
            messageReceivedFromBetsyncCounter.increment();

            websocketMessageProcessing.process(betSyncMessage.toString());
            betSyncMessage = new StringBuilder();
        } else {
            log.debug("we have received a partial message starting with {}", message.getPayload());
        }
    }


    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
