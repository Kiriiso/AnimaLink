package com.vet.animalink.control_alta_service.client;

import com.vet.animalink.control_alta_service.client.dto.MascotaApiResponse;
import com.vet.animalink.control_alta_service.client.dto.MascotaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class MascotaClient {

    private final WebClient webClient;

    public MascotaClient(WebClient.Builder builder,
                         @Value("${services.mascota.url}") String mascotaServiceUrl) {
        this.webClient = builder.baseUrl(mascotaServiceUrl).build();
    }

    public Optional<MascotaDTO> obtenerMascota(Long mascotaId) {
        try {
            MascotaApiResponse resp = webClient.get()
                    .uri("/api/v1/mascotas/{id}", mascotaId)
                    .retrieve()
                    .bodyToMono(MascotaApiResponse.class)
                    .block();
            return Optional.ofNullable(resp).map(MascotaApiResponse::data);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        }
    }
}
