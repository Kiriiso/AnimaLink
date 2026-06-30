package com.vet.animalink.mascota_service.client;

import com.vet.animalink.mascota_service.client.dto.ClienteApiResponse;
import com.vet.animalink.mascota_service.client.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Cliente HTTP (WebClient) que consulta el microservicio cliente_service.
 * Demuestra la COMUNICACIÓN entre microservicios exigida por la rúbrica.
 */
@Component
public class ClienteClient {

    private final WebClient webClient;

    public ClienteClient(WebClient.Builder builder,
                         @Value("${services.cliente.url}") String clienteServiceUrl) {
        this.webClient = builder.baseUrl(clienteServiceUrl).build();
    }

    /**
     * Devuelve el cliente si existe; Optional.empty() si cliente_service responde 404.
     */
    public Optional<ClienteDTO> obtenerCliente(Long clienteId) {
        try {
            ClienteApiResponse respuesta = webClient.get()
                    .uri("/api/v1/clientes/{id}", clienteId)
                    .retrieve()
                    .bodyToMono(ClienteApiResponse.class)
                    .block();
            return Optional.ofNullable(respuesta).map(ClienteApiResponse::data);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        }
    }
}
