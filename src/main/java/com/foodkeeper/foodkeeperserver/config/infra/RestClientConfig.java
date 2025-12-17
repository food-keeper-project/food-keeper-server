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

    private static final int CLOVA_CONNECT_TIMEOUT = 5;
    private static final int CLOVA_READ_TIMEOUT = 20;

    @Bean("APIRestClient")
    @Primary
    public RestClient commonRestClient() {
        return RestClient.builder()
                .requestFactory(createFactory(API_CONNECT_TIMEOUT, API_READ_TIMEOUT))
                .build();
    }

    @Bean("clovaRestClient")
    public RestClient clovaRestClient() {
        return RestClient.builder()
                .requestFactory(createFactory(CLOVA_CONNECT_TIMEOUT, CLOVA_READ_TIMEOUT))
                .build();
    }

    private SimpleClientHttpRequestFactory createFactory(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(connectTimeout).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(readTimeout).toMillis());
        return factory;
    }
}
