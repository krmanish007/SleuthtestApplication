package com.sample.manish.sleuthtest.mockWebsocketServer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MockWebSocketHandler extends TextWebSocketHandler {

    @Getter
    private boolean connectionEstablished = false;

    public void setSendMessage(String sendMessage) throws IOException {
        if (session != null) {
            session.sendMessage(new TextMessage(sendMessage));
        }
    }

    private  WebSocketSession session;

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        log.info("test web socket connection is established");
        this.session = session;
        connectionEstablished = true;
    }

    public void closeConnection() throws IOException {
        log.info("session closed");
        this.session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
