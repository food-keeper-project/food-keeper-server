package com.foodkeeper.foodkeeperserver.config.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    //todo 타임아웃 시간 재설정(AI,KAKAO)
    private static final int CONNECTION_TIMEOUT_SECONDS = 5;
    private static final int READ_TIMEOUT_SECONDS = 20;

    @Bean
    public RestClient restClient() {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(READ_TIMEOUT_SECONDS).toMillis());

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
