package com.foodkeeper.foodkeeperserver.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(openApiInfo());
    }

    private Info openApiInfo() {
        return new Info()
                .title("Food keeper API")
                .description("Food keeper API 명세서")
                .version("1.0");
    }
}