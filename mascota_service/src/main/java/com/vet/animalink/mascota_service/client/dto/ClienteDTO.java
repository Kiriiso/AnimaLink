package com.vet.animalink.mascota_service.client.dto;

/**
 * Vista parcial del cliente que devuelve cliente_service.
 * Solo tomamos los campos que este microservicio necesita (los demás se ignoran).
 */
public record ClienteDTO(
        Long id,
        String nombre,
        String apellido
) {
    public String nombreCompleto() {
        return ((nombre == null ? "" : nombre) + " " + (apellido == null ? "" : apellido)).trim();
    }
}
