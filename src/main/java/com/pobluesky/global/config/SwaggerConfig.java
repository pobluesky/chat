package com.pobluesky.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info=@Info(title = "포청천 Swagger UI",
        description = "customer-inquiry-system API 명세서",
        version ="v1"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("apiKeyAuth");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("apiKeyAuth", securityScheme))
            .security(Arrays.asList(securityRequirement));
    }
}