package com.vet.animalink.control_alta_service.client.dto;

/** Vista parcial de la mascota devuelta por mascota_service. */
public record MascotaDTO(
        Long id,
        String nombre,
        String especie
) {}
