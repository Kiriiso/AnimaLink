package com.vet.animalink.mascota_service.mapper;

import com.vet.animalink.mascota_service.client.dto.ClienteDTO;
import com.vet.animalink.mascota_service.dto.MascotaRequest;
import com.vet.animalink.mascota_service.dto.MascotaResponse;
import com.vet.animalink.mascota_service.model.Mascota;
import org.springframework.stereotype.Component;

@Component
public class MascotaMapper {

    /** Entidad -> DTO de salida (sin nombre de dueño). */
    public MascotaResponse toResponse(Mascota m) {
        return toResponse(m, null);
    }

    /** Entidad + datos del dueño (de cliente_service) -> DTO de salida enriquecido. */
    public MascotaResponse toResponse(Mascota m, ClienteDTO dueno) {
        return new MascotaResponse(
                m.getId(),
                m.getNombre(),
                m.getEspecie() != null ? m.getEspecie().name() : null,
                m.getRaza(),
                m.getSexo() != null ? m.getSexo().name() : null,
                m.getFechaNacimiento(),
                m.getColor(),
                m.getClienteId(),
                dueno != null ? dueno.nombreCompleto() : null
        );
    }

    /** DTO de entrada -> Entidad nueva. */
    public Mascota toEntity(MascotaRequest req) {
        return Mascota.builder()
                .nombre(req.nombre())
                .especie(req.especie())
                .raza(req.raza())
                .sexo(req.sexo())
                .fechaNacimiento(req.fechaNacimiento())
                .color(req.color())
                .clienteId(req.clienteId())
                .activo(true)
                .build();
    }
}
