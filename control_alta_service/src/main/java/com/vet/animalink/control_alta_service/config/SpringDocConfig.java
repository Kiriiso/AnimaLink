package com.vet.animalink.control_alta_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AnimaLink - Control Alta Service API",
                version = "v1",
                description = "Microservicio de control de hospitalización (ingresos/altas) del sistema veterinario AnimaLink.",
                contact = @Contact(name = "Equipo AnimaLink", email = "equipo@animalink.cl")
        ),
        servers = @Server(url = "/")
)
public class SpringDocConfig {
}
