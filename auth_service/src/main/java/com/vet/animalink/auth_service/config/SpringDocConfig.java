package com.vet.animalink.auth_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AnimaLink - Auth Service API",
                version = "v1",
                description = "Microservicio de autenticación y seguridad (JWT) del sistema veterinario AnimaLink.",
                contact = @Contact(name = "Equipo AnimaLink", email = "equipo@animalink.cl")
        ),
        servers = @Server(url = "/")
)
public class SpringDocConfig {
}
