package com.vet.animalink.cita_service.mapper;

import com.vet.animalink.cita_service.client.dto.MascotaDTO;
import com.vet.animalink.cita_service.client.dto.UsuarioDTO;
import com.vet.animalink.cita_service.dto.CitaRequest;
import com.vet.animalink.cita_service.dto.CitaResponse;
import com.vet.animalink.cita_service.model.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaResponse toResponse(Cita c) {
        return toResponse(c, null, null);
    }

    public CitaResponse toResponse(Cita c, MascotaDTO mascota, UsuarioDTO veterinario) {
        return new CitaResponse(
                c.getId(),
                c.getMascotaId(),
                mascota != null ? mascota.nombre() : null,
                c.getVeterinarioId(),
                veterinario != null ? veterinario.nombreCompleto() : null,
                c.getFechaHora(),
                c.getMotivo(),
                c.getEstado() != null ? c.getEstado().name() : null,
                c.getObservaciones()
        );
    }

    public Cita toEntity(CitaRequest req) {
        return Cita.builder()
                .mascotaId(req.mascotaId())
                .veterinarioId(req.veterinarioId())
                .fechaHora(req.fechaHora())
                .motivo(req.motivo())
                .observaciones(req.observaciones())
                .build();
    }
}
