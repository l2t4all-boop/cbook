package com.l2t.cbook.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Contact Book API")
                        .description("REST API for managing contacts")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("L2T")
                                .email("support@l2t.com")))
                .addSecurityItem(new SecurityRequirement().addList("X-API-KEY"))
                .components(new Components()
                        .addSecuritySchemes("X-API-KEY", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-API-KEY")));
    }
}