package com.sample.manish.sleuthtest.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrometheusMetricConfig {

    @Autowired
    private MeterRegistry defaultRegistry;

    @Bean
    public Counter messageReceivedFromBetsyncCounter() {
        return Counter.builder("counter_message_received_from_websocket")
                .description("number of complete message received from websocket")
                .register(defaultRegistry);
    }
}
