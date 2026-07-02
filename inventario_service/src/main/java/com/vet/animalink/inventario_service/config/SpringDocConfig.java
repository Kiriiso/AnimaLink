package com.vet.animalink.inventario_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AnimaLink - Inventario Service API",
                version = "v1",
                description = "Microservicio de inventario (insumos y medicamentos) del sistema veterinario AnimaLink.",
                contact = @Contact(name = "Equipo AnimaLink", email = "equipo@animalink.cl")
        ),
        servers = @Server(url = "/")
)
public class SpringDocConfig {
}
