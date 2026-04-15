package io.github.codewithwasif.techhire.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(
                        new Info().title("Tech-Hire APIs")
                                .description("This is my API documentation.  \n"
                                        + "You can find more about me on [GitHub](https://github.com/code-with-wasif).")
                )

                .servers(List.of(
                        new Server().url("https://code-with-wasif-techhire-api.hf.space").description("Production server"),
                        new Server().url("http://localhost:8080").description("Local development")
                ))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));

    }
}

