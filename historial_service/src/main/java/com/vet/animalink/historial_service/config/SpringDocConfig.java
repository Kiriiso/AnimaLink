package com.vet.animalink.historial_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AnimaLink - Historial Service API",
                version = "v1",
                description = "Microservicio de historial clínico del sistema veterinario AnimaLink.",
                contact = @Contact(name = "Equipo AnimaLink", email = "equipo@animalink.cl")
        ),
        servers = @Server(url = "/")
)
public class SpringDocConfig {
}
