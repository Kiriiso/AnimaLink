package com.vet.appointment_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PetsClient {

    private final RestTemplate rest;

    public PetsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public Object getPetById(Long id) {
        String url = "http://localhost:8084/pets/" + id;
        return rest.getForObject(url, Object.class);
    }
}
