package com.vet.animalink.cita_service.client.dto;

/** Vista parcial del usuario/veterinario devuelta por usuario_service. */
public record UsuarioDTO(
        Long id,
        String nombre,
        String apellido,
        String rol
) {
    public String nombreCompleto() {
        return ((nombre == null ? "" : nombre) + " " + (apellido == null ? "" : apellido)).trim();
    }
}
