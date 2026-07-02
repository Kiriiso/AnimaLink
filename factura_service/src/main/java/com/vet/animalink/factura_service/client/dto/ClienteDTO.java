package com.vet.animalink.factura_service.client.dto;

/** Vista parcial del cliente devuelta por cliente_service. */
public record ClienteDTO(
        Long id,
        String nombre,
        String apellido
) {
    public String nombreCompleto() {
        return ((nombre == null ? "" : nombre) + " " + (apellido == null ? "" : apellido)).trim();
    }
}
