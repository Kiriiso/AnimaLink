package com.vet.animalink.usuario_service.mapper;

import com.vet.animalink.usuario_service.dto.UsuarioRequest;
import com.vet.animalink.usuario_service.dto.UsuarioResponse;
import com.vet.animalink.usuario_service.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTelefono(),
                u.getRol() != null ? u.getRol().name() : null,
                u.getEspecialidad(),
                enmascararRut(u.getRut())
        );
    }

    public Usuario toEntity(UsuarioRequest req) {
        return Usuario.builder()
                .rut(req.rut())
                .nombre(req.nombre())
                .apellido(req.apellido())
                .email(req.email())
                .telefono(req.telefono())
                .rol(req.rol())
                .especialidad(req.especialidad())
                .activo(true)
                .build();
    }

    private String enmascararRut(String rut) {
        if (rut == null || !rut.contains("-")) {
            return "********";
        }
        return "********-" + rut.substring(rut.indexOf('-') + 1);
    }
}
