package com.foodkeeper.foodkeeperserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private static final int CONNECTION_TIMEOUT_SECONDS = 3;
    private static final int READ_TIMEOUT_SECONDS = 2;

    @Bean
    public RestClient restClient() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(READ_TIMEOUT_SECONDS).toMillis());

        return RestClient.builder().requestFactory(factory).build();
    }
}
