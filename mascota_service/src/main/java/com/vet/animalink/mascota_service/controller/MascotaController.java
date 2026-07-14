
package com.vet.animalink.mascota_service.controller;

import com.vet.animalink.mascota_service.assembler.MascotaModelAssembler;
import com.vet.animalink.mascota_service.dto.ApiResponse;
import com.vet.animalink.mascota_service.dto.MascotaRequest;
import com.vet.animalink.mascota_service.dto.MascotaResponse;
import com.vet.animalink.mascota_service.mapper.MascotaMapper;
import com.vet.animalink.mascota_service.model.Mascota;
import com.vet.animalink.mascota_service.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mascotas", description = "Gestión de mascotas de la veterinaria")
@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;
    private final MascotaMapper mascotaMapper;
    private final MascotaModelAssembler assembler;

    public MascotaController(MascotaService mascotaService, MascotaMapper mascotaMapper,
                             MascotaModelAssembler assembler) {
        this.mascotaService = mascotaService;
        this.mascotaMapper = mascotaMapper;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todas las mascotas (con enlaces HATEOAS)")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<MascotaResponse>>>> listar() {
        List<MascotaResponse> lista = mascotaService.listar().stream()
                .map(mascotaMapper::toResponse)
                .toList();
        String msg = lista.isEmpty() ? "No hay mascotas registradas" : "Se encontraron " + lista.size() + " mascota(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, assembler.toCollection(lista)));
    }

    @Operation(summary = "Obtener una mascota por su ID (incluye datos del dueño y enlaces HATEOAS)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<MascotaResponse>>> obtener(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerPorId(id);
        // Enriquece la respuesta consultando cliente_service (flujo entre microservicios)
        MascotaResponse data = mascotaService.obtenerDueno(mascota.getClienteId())
                .map(dueno -> mascotaMapper.toResponse(mascota, dueno))
                .orElseGet(() -> mascotaMapper.toResponse(mascota));
        return ResponseEntity.ok(ApiResponse.ok("Mascota encontrada correctamente", assembler.toModel(data))); 
    }

    @Operation(summary = "Registrar una nueva mascota (valida que el dueño exista)")
    @PostMapping
    public ResponseEntity<ApiResponse<MascotaResponse>> crear(@Valid @RequestBody MascotaRequest request) {
        Mascota creada = mascotaService.crear(mascotaMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Mascota registrada correctamente", mascotaMapper.toResponse(creada)));
    }

    @Operation(summary = "Actualizar una mascota existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MascotaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaRequest request) {
        Mascota actualizada = mascotaService.actualizar(id, mascotaMapper.toEntity(request));
        return ResponseEntity.ok(
                ApiResponse.ok("Mascota actualizada correctamente", mascotaMapper.toResponse(actualizada)));
    }

    @Operation(summary = "Eliminar una mascota")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        mascotaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Mascota eliminada correctamente", null));
    }
}
