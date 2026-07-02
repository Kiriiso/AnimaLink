package com.vet.animalink.historial_service.client.dto;

/** Vista parcial de la mascota devuelta por mascota_service. */
public record MascotaDTO(
        Long id,
        String nombre,
        String especie
) {}
