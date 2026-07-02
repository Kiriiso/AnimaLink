package com.vet.animalink.cita_service.client;

import com.vet.animalink.cita_service.client.dto.UsuarioApiResponse;
import com.vet.animalink.cita_service.client.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder builder,
                         @Value("${services.usuario.url}") String usuarioServiceUrl) {
        this.webClient = builder.baseUrl(usuarioServiceUrl).build();
    }

    public Optional<UsuarioDTO> obtenerUsuario(Long usuarioId) {
        try {
            UsuarioApiResponse resp = webClient.get()
                    .uri("/api/v1/usuarios/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(UsuarioApiResponse.class)
                    .block();
            return Optional.ofNullable(resp).map(UsuarioApiResponse::data);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        }
    }
}
