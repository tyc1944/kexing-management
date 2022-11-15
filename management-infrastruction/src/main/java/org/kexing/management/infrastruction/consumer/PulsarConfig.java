package org.kexing.management.infrastruction.consumer;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PulsarConfig {

    @Value("${pulsar-proxy.iot.ip}")
    String ip;

    @Bean
    PulsarClient pulsarClient() throws PulsarClientException {
        int cores = Runtime.getRuntime().availableProcessors();
        return  PulsarClient.builder()
                .serviceUrl("pulsar://"+ip+":6650")
                .ioThreads(Math.max(1,cores/4))
                .listenerThreads(cores)
                .build();
    }
}
