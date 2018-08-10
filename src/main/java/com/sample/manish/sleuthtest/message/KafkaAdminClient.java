package com.sample.manish.sleuthtest.message;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaAdminClient {

    private final AdminClient kafkaAdmin;
    public static final long MAX_TIME_LIMIT_TO_CONNECT = 5;

    public boolean isKafkaBrokerRunning() {
        try {
            if (kafkaAdmin.describeCluster()
                    .clusterId()
                    .get(MAX_TIME_LIMIT_TO_CONNECT, TimeUnit.SECONDS)
                    != null) {
                return true;
            }
        }
        catch (TimeoutException e) {
            log.error("timeout occurred while communicating to kafka broker");
        }
        catch (Exception e) {
            log.error("exception occurred while communicating to kafka broker", e);
        }
        return false;
    }
}
