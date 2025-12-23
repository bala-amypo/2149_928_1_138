package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Room Key Digital share")
                        .description("Swagger CRUD APIs")
                        .version("1.0"))
                
                .servers(List.of(
                        new Server().url("https://9163.408procr.amypo.ai/")
                ));
    }
}