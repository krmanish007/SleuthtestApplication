package com.sample.manish.sleuthtest.mockWebsocketServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class MockWebSocketServerConfig implements WebSocketConfigurer {

    @Autowired
    private MockWebSocketHandler mockWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mockWebSocketHandler, "/testwebsocket");
    }
}