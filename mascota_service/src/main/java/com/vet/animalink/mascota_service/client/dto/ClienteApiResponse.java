package com.vet.animalink.mascota_service.client.dto;

/**
 * Envoltorio que devuelve cliente_service ({ status, success, message, data, timestamp }).
 * Solo nos interesa el campo "data"; el resto lo ignora Jackson.
 */
public record ClienteApiResponse(
        ClienteDTO data
) {}
