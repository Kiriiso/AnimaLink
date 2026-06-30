package com.vet.animalink.usuario_service.controller;

import com.vet.animalink.usuario_service.dto.ApiResponse;
import com.vet.animalink.usuario_service.dto.UsuarioResponse;
import com.vet.animalink.usuario_service.exception.ResourceNotFoundException;
import com.vet.animalink.usuario_service.mapper.UsuarioMapper;
import com.vet.animalink.usuario_service.model.enums.Rol;
import com.vet.animalink.usuario_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios V2", description = "Búsquedas y filtros avanzados de usuarios")
@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioControllerV2(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @Operation(summary = "Listar usuarios por rol (ej: VETERINARIO)")
    @GetMapping("/rol/{rol}")
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> porRol(@PathVariable Rol rol) {
        List<UsuarioResponse> data = usuarioService.listarPorRol(rol).stream()
                .map(usuarioMapper::toResponse)
                .toList();
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("No hay usuarios con el rol " + rol);
        }
        return ResponseEntity.ok(ApiResponse.ok("Usuarios con rol " + rol + ": " + data.size(), data));
    }

    @Operation(summary = "Buscar usuarios por nombre o apellido")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> buscar(@RequestParam String texto) {
        List<UsuarioResponse> data = usuarioService.buscarPorNombre(texto).stream()
                .map(usuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Coincidencias: " + data.size(), data));
    }
}
