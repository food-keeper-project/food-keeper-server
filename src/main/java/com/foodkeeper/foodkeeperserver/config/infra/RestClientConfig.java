package com.foodkeeper.foodkeeperserver.config.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private static final int API_CONNECT_TIMEOUT = 5;
    private static final int API_READ_TIMEOUT = 3;

    @Bean("commonRestClient")
    @Primary
    public RestClient commonRestClient() {
        return RestClient.builder()
                .requestFactory(createFactory(API_CONNECT_TIMEOUT, API_READ_TIMEOUT))
                .build();
    }

    private SimpleClientHttpRequestFactory createFactory(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(connectTimeout).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(readTimeout).toMillis());
        return factory;
    }
}
