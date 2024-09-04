package com.omar.backend.mymentor.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST MY-Mentor")
                        .version("1.0")
                        .description("Documentación de mi API REST MY-Mentor con Spring Boot 3"));
    }
}