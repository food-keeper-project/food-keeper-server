package com.foodkeeper.foodkeeperserver.clova.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ClovaService {

    @Qualifier("clovaRestClient")
    private final RestClient restClient;


    @Value("${clova.url}")
    private String url;

    @Value("${clova.api-key}")
    private String apiKey;

    @Value("${clova.gateway-key")
    private String gatewayKey;


}
