package com.vet_animalink.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final WebClient webClient;

    public GatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> clientes(@PathVariable Long id) {
        Object body = webClient.get()
                .uri("lb://cliente_service/clientes/{id}", id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<?> pets(@PathVariable Long id) {
        Object body = webClient.get()
                .uri("lb://pets_service/pets/{id}", id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(body);
    }

}
