package com.vet.animalink.usuario_service.controller;

import com.vet.animalink.usuario_service.assembler.UsuarioModelAssembler;
import com.vet.animalink.usuario_service.dto.ApiResponse;
import com.vet.animalink.usuario_service.dto.UsuarioRequest;
import com.vet.animalink.usuario_service.dto.UsuarioResponse;
import com.vet.animalink.usuario_service.mapper.UsuarioMapper;
import com.vet.animalink.usuario_service.model.Usuario;
import com.vet.animalink.usuario_service.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Gestión del personal del sistema (veterinarios, recepcionistas, admin)")
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;
    private final UsuarioModelAssembler assembler;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper,
                             UsuarioModelAssembler assembler) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los usuarios (con enlaces HATEOAS)")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<UsuarioResponse>>>> listar() {
        List<UsuarioResponse> lista = usuarioService.listar().stream()
                .map(usuarioMapper::toResponse)
                .toList();
        String msg = lista.isEmpty() ? "No hay usuarios registrados" : "Se encontraron " + lista.size() + " usuario(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, assembler.toCollection(lista)));
    }

    @Operation(summary = "Obtener un usuario por su ID (con enlaces HATEOAS)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<UsuarioResponse>>> obtener(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(
                ApiResponse.ok("Usuario encontrado correctamente", assembler.toModel(usuarioMapper.toResponse(usuario))));
    }

    @Operation(summary = "Crear un nuevo usuario/funcionario")
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> crear(@Valid @RequestBody UsuarioRequest request) {
        Usuario creado = usuarioService.crear(usuarioMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Usuario creado correctamente", usuarioMapper.toResponse(creado)));
    }

    @Operation(summary = "Actualizar un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        Usuario actualizado = usuarioService.actualizar(id, usuarioMapper.toEntity(request));
        return ResponseEntity.ok(
                ApiResponse.ok("Usuario actualizado correctamente", usuarioMapper.toResponse(actualizado)));
    }

    @Operation(summary = "Eliminar un usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado correctamente", null));
    }
}
