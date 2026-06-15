package com.vet.appointment_service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClienteClient {

    private final RestTemplate rest;

    public ClienteClient(RestTemplate rest) {
        this.rest = rest;
    }

    public Object getClienteById(Long id) {
        String url = "http://localhost:8083/clientes/" + id;
        return rest.getForObject(url, Object.class);
    }
}
