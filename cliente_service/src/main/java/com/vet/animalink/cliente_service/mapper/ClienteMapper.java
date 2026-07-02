package com.vet.animalink.cliente_service.mapper;

import com.vet.animalink.cliente_service.dto.ClienteRequest;
import com.vet.animalink.cliente_service.dto.ClienteResponse;
import com.vet.animalink.cliente_service.model.Cliente;
import org.springframework.stereotype.Component;

/**
 * Convierte entre la entidad (model) y los DTO.
 * Aquí se decide qué información se expone y cómo se protege la sensible.
 */
@Component
public class ClienteMapper {

    /** Entidad -> DTO de salida (oculta/enmascara datos sensibles). */
    public ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(),
                c.getNombre(),
                c.getApellido(),
                c.getEmail(),
                c.getTelefono(),
                c.getDireccion(),
                enmascararRut(c.getRut())
        );
    }

    /** DTO de entrada -> Entidad nueva. */
    public Cliente toEntity(ClienteRequest req) {
        return Cliente.builder()
                .rut(req.rut())
                .nombre(req.nombre())
                .apellido(req.apellido())
                .email(req.email())
                .telefono(req.telefono())
                .direccion(req.direccion())
                .activo(true)
                .build();
    }

    /**
     * Enmascara el RUT dejando visible solo el dígito verificador.
     * Ej: 18345678-9  ->  ********-9
     */
    private String enmascararRut(String rut) {
        if (rut == null || !rut.contains("-")) {
            return "********";
        }
        String dv = rut.substring(rut.indexOf('-') + 1);
        return "********-" + dv;
    }
}
