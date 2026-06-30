package com.vet.animalink.control_alta_service.mapper;

import com.vet.animalink.control_alta_service.client.dto.MascotaDTO;
import com.vet.animalink.control_alta_service.dto.ControlAltaRequest;
import com.vet.animalink.control_alta_service.dto.ControlAltaResponse;
import com.vet.animalink.control_alta_service.model.ControlAlta;
import org.springframework.stereotype.Component;

@Component
public class ControlAltaMapper {

    public ControlAltaResponse toResponse(ControlAlta c) {
        return toResponse(c, null);
    }

    public ControlAltaResponse toResponse(ControlAlta c, MascotaDTO mascota) {
        return new ControlAltaResponse(
                c.getId(),
                c.getMascotaId(),
                mascota != null ? mascota.nombre() : null,
                c.getFechaIngreso(),
                c.getFechaAlta(),
                c.getMotivoIngreso(),
                c.getEstado() != null ? c.getEstado().name() : null,
                c.getObservaciones()
        );
    }

    public ControlAlta toEntity(ControlAltaRequest req) {
        return ControlAlta.builder()
                .mascotaId(req.mascotaId())
                .motivoIngreso(req.motivoIngreso())
                .observaciones(req.observaciones())
                .build();
    }
}
