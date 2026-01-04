package com.foodkeeper.foodkeeperserver.config.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class MapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
                        .build();
    }
}
