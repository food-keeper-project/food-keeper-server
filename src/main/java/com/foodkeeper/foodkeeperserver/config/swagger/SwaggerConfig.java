package com.foodkeeper.foodkeeperserver.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${client.url}")
    private String clientUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "bearerAuth";
        SecurityScheme scheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        SecurityRequirement requirement = new SecurityRequirement().addList(securitySchemeName);

        return new OpenAPI()
                .info(openApiInfo())
                .servers(List.of(new Server().url(clientUrl)))
                .components(new Components().addSecuritySchemes(securitySchemeName, scheme))
                .addSecurityItem(requirement);
    }

    private Info openApiInfo() {
        return new Info()
                .title("Food keeper API")
                .description("Food keeper API 명세서")
                .version("1.0");
    }
}