package com.vet_animalink.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final RestTemplate rest;

    public GatewayController(RestTemplate rest) {
        this.rest = rest;
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> clientes(@PathVariable Long id) {
        String url = "http://localhost:8083/clientes/" + id;
        return rest.getForEntity(URI.create(url), Object.class);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<?> pets(@PathVariable Long id) {
        String url = "http://localhost:8084/pets/" + id;
        return rest.getForEntity(URI.create(url), Object.class);
    }

}
