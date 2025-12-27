package com.foodkeeper.foodkeeperserver.config.infra;

import com.foodkeeper.foodkeeperserver.recipe.controller.v1.ClovaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private static final int API_CONNECT_TIMEOUT = 5;
    private static final int API_READ_TIMEOUT = 3;

    private static final int CLOVA_CONNECT_TIMEOUT = 5;
    private static final int CLOVA_READ_TIMEOUT = 20;


    @Value("${clova.url}")
    private String url;

    @Bean("commonRestClient")
    @Primary
    public RestClient commonRestClient() {
        return RestClient.builder()
                .requestFactory(createFactory(API_CONNECT_TIMEOUT, API_READ_TIMEOUT))
                .build();
    }

    @Bean
    public ClovaClient clovaClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(url)
                .requestFactory(createFactory(CLOVA_CONNECT_TIMEOUT, CLOVA_READ_TIMEOUT))
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(ClovaClient.class);
    }

    private SimpleClientHttpRequestFactory createFactory(int connectTimeout, int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(connectTimeout).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(readTimeout).toMillis());
        return factory;
    }
}
