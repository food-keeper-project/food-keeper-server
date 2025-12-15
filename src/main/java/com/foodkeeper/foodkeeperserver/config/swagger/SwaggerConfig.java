package com.foodkeeper.foodkeeperserver.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme scheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        SecurityRequirement requirement = new SecurityRequirement().addList("JWT");

        return new OpenAPI()
                .info(openApiInfo())
                .components(new Components().addSecuritySchemes("Bearer", scheme))
                .addSecurityItem(requirement);
    }

    private Info openApiInfo() {
        return new Info()
                .title("Food keeper API")
                .description("Food keeper API 명세서")
                .version("1.0");
    }
}