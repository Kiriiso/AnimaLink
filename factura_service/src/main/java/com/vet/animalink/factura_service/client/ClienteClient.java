package com.vet.animalink.factura_service.client;

import com.vet.animalink.factura_service.client.dto.ClienteApiResponse;
import com.vet.animalink.factura_service.client.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class ClienteClient {

    private final WebClient webClient;

    public ClienteClient(WebClient.Builder builder,
                         @Value("${services.cliente.url}") String clienteServiceUrl) {
        this.webClient = builder.baseUrl(clienteServiceUrl).build();
    }

    public Optional<ClienteDTO> obtenerCliente(Long clienteId) {
        try {
            ClienteApiResponse resp = webClient.get()
                    .uri("/api/v1/clientes/{id}", clienteId)
                    .retrieve()
                    .bodyToMono(ClienteApiResponse.class)
                    .block();
            return Optional.ofNullable(resp).map(ClienteApiResponse::data);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        }
    }
}
