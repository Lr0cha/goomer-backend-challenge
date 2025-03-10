package com.api.goomer.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("REST API - Spring Goomer Lista Rango")
                                .description("API para e gerenciar os restaurantes e os produtos do seu cardápio (desafio técnico).")
                                .version("v1"));
    }
}
