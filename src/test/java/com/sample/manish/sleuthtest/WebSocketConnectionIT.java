package com.sample.manish.sleuthtest;

import com.sample.manish.sleuthtest.mockWebsocketServer.MockWebSocketHandler;
import com.sample.manish.sleuthtest.mockWebsocketServer.MockWebSocketServerConfig;
import javafx.application.Application;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("it")
@EmbeddedKafka(partitions = 1, controlledShutdown = true,
        brokerProperties = {"listeners=PLAINTEXT://localhost:3335", "port=3335"})
@Import(MockWebSocketServerConfig.class)
public class WebSocketConnectionIT {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Autowired
    private MockWebSocketHandler mockAmelcoWebSocketHandler;

    @Test
    public void testServer_started() {
        assertTrue(isConditionMet(20, i-> mockAmelcoWebSocketHandler.isConnectionEstablished()));
    }

    @Test
    public void marshalling_exception_for_invalidMessage_from_server() throws IOException, InterruptedException {
        String message = "This is my first Message";
        int i=0;
        while (i++<5000) {
            TimeUnit.MILLISECONDS.sleep(10);
            if(i == 2000) {
                mockAmelcoWebSocketHandler.closeConnection();
            }
            if (i < 2000) {
                mockAmelcoWebSocketHandler.setSendMessage(message + i);
            }
        }
        assertTrue(isConditionMet(25, counter -> outputCapture.toString().contains("websocket message- " + message)));
    }

    private boolean isConditionMet(final int maxAttempt, IntPredicate predicate) {
        return IntStream.rangeClosed(0, maxAttempt)
                .peek(a -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        fail();
                    }
                })
                .anyMatch(predicate);
    }
}
