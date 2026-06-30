package com.vet.animalink.historial_service.mapper;

import com.vet.animalink.historial_service.client.dto.MascotaDTO;
import com.vet.animalink.historial_service.dto.HistorialRequest;
import com.vet.animalink.historial_service.dto.HistorialResponse;
import com.vet.animalink.historial_service.model.Historial;
import org.springframework.stereotype.Component;

@Component
public class HistorialMapper {

    public HistorialResponse toResponse(Historial h) {
        return toResponse(h, null);
    }

    public HistorialResponse toResponse(Historial h, MascotaDTO mascota) {
        return new HistorialResponse(
                h.getId(),
                h.getMascotaId(),
                mascota != null ? mascota.nombre() : null,
                h.getFecha(),
                h.getDiagnostico(),
                h.getTratamiento(),
                h.getObservaciones()
        );
    }

    public Historial toEntity(HistorialRequest req) {
        return Historial.builder()
                .mascotaId(req.mascotaId())
                .fecha(req.fecha())
                .diagnostico(req.diagnostico())
                .tratamiento(req.tratamiento())
                .observaciones(req.observaciones())
                .build();
    }
}
