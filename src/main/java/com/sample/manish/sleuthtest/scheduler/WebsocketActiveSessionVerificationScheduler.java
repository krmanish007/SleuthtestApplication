package com.sample.manish.sleuthtest.scheduler;

import com.sample.manish.sleuthtest.websocket.WebsocketAdapter;
import io.micrometer.core.instrument.Counter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This scheduler keep looking the session, and if one doesn't exist in open state, it creates a new connection and new session.
 * It also applies a sleepover policy before trying a retry, and also check a retry limit.
 */
@Service
@Slf4j
@AllArgsConstructor
public class WebsocketActiveSessionVerificationScheduler {

    private final WebsocketAdapter websocketAdapter;

    @Qualifier("continuousRetryCounterForFailedReconnection")
    private Counter continuousRetryCounterForFailedReconnection;
    private static boolean serverStarted = false;

    @Scheduled(initialDelay = 5000,
            fixedDelay = 5000)
    public void restartSessionForInactiveSession() {
        log.error("slow connection closed (if any still open) to start a new session");

        if (!serverStarted) {
            return;
        }

        if (!websocketAdapter.isSessionActive()) {
            try {
                websocketAdapter.closeSession();
            } catch (IOException e) {
                log.error("exception occurred while closing the websocket connection. Manual intervention required!!!!. " +
                        "See below for detailed IO exception:", e);
            }

            sleepBeforeTryingNewConnection();

            continuousRetryCounterForFailedReconnection.increment();

            websocketAdapter.startNewConnection();

            log.error("slow connection closed (if any still open) to start a new session");
        }
    }

    private void sleepBeforeTryingNewConnection() {

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            log.error("interruption occurred while sleeping with exception", e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onServerStart() {
        serverStarted = true;
    }

}