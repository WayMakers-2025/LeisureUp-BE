package org.leisureup.global.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swagger() {
        return new OpenAPI()
                .components(components())
                .info(info())
                .addSecurityItem(
                        new SecurityRequirement().addList("Authorization")
                );
    }

    @Bean
    public Components components() {
        return new Components()
                .addSecuritySchemes(
                        "Authorization", jwtSecurityScheme()
                );
    }

    @Bean
    public SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    @Bean
    public Info info() {
        return new Info()
                .title("LeisureUp - backend")
                .version("v1")
                .description("API specification for LeisureUp backend application");
    }
}
