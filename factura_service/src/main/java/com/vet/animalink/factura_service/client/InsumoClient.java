package com.vet.animalink.factura_service.client;

import com.vet.animalink.factura_service.client.dto.InsumoApiResponse;
import com.vet.animalink.factura_service.client.dto.InsumoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

/**
 * Cliente HTTP hacia inventario_service. Lee precios/stock y descuenta stock al facturar
 * (comunicación entre microservicios con consulta Y escritura).
 */
@Component
public class InsumoClient {

    private final WebClient webClient;

    public InsumoClient(WebClient.Builder builder,
                        @Value("${services.inventario.url}") String inventarioServiceUrl) {
        this.webClient = builder.baseUrl(inventarioServiceUrl).build();
    }

    public Optional<InsumoDTO> obtenerInsumo(Long insumoId) {
        try {
            InsumoApiResponse resp = webClient.get()
                    .uri("/api/v1/insumos/{id}", insumoId)
                    .retrieve()
                    .bodyToMono(InsumoApiResponse.class)
                    .block();
            return Optional.ofNullable(resp).map(InsumoApiResponse::data);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        }
    }

    /**
     * Descuenta 'cantidad' unidades del stock del insumo en inventario_service.
     * Reenvía el token JWT del usuario que emite la factura (propagación de credenciales
     * entre microservicios) porque descontar stock es una operación protegida por rol.
     * Lanza IllegalArgumentException si no hay stock suficiente (400 desde inventario).
     */
    public void descontarStock(Long insumoId, int cantidad, String authorizationHeader) {
        try {
            webClient.patch()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/insumos/{id}/stock")
                            .queryParam("cantidad", -Math.abs(cantidad))
                            .build(insumoId))
                    .headers(h -> {
                        if (authorizationHeader != null && !authorizationHeader.isBlank()) {
                            h.set("Authorization", authorizationHeader);
                        }
                    })
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.BadRequest e) {
            throw new IllegalArgumentException("Stock insuficiente para el insumo " + insumoId);
        } catch (WebClientResponseException.Unauthorized | WebClientResponseException.Forbidden e) {
            throw new IllegalArgumentException("Sin permiso para descontar stock del insumo " + insumoId
                    + " (se requiere un token con rol autorizado)");
        }
    }
}
