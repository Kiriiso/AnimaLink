package com.vet.animalink.cita_service.controller;

import com.vet.animalink.cita_service.dto.ApiResponse;
import com.vet.animalink.cita_service.dto.CitaRequest;
import com.vet.animalink.cita_service.dto.CitaResponse;
import com.vet.animalink.cita_service.mapper.CitaMapper;
import com.vet.animalink.cita_service.model.Cita;
import com.vet.animalink.cita_service.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Citas", description = "Gestión de citas/agendamiento")
@RestController
@RequestMapping("/api/v1/citas")
public class CitaController {

    private final CitaService citaService;
    private final CitaMapper citaMapper;

    public CitaController(CitaService citaService, CitaMapper citaMapper) {
        this.citaService = citaService;
        this.citaMapper = citaMapper;
    }

    @Operation(summary = "Listar todas las citas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CitaResponse>>> listar() {
        List<CitaResponse> data = citaService.listar().stream()
                .map(citaMapper::toResponse)
                .toList();
        String msg = data.isEmpty() ? "No hay citas registradas" : "Se encontraron " + data.size() + " cita(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, data));
    }

    @Operation(summary = "Obtener una cita por su ID (incluye mascota y veterinario)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponse>> obtener(@PathVariable Long id) {
        Cita cita = citaService.obtenerPorId(id);
        // Enriquecimiento desde mascota_service y usuario_service
        CitaResponse data = citaMapper.toResponse(
                cita,
                citaService.obtenerMascota(cita.getMascotaId()).orElse(null),
                citaService.obtenerVeterinario(cita.getVeterinarioId()).orElse(null));
        return ResponseEntity.ok(ApiResponse.ok("Cita encontrada correctamente", data));
    }

    @Operation(summary = "Agendar una nueva cita (valida mascota y veterinario)")
    @PostMapping
    public ResponseEntity<ApiResponse<CitaResponse>> crear(@Valid @RequestBody CitaRequest request) {
        Cita creada = citaService.crear(citaMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Cita agendada correctamente", citaMapper.toResponse(creada)));
    }

    @Operation(summary = "Actualizar una cita existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaRequest request) {
        Cita actualizada = citaService.actualizar(id, citaMapper.toEntity(request));
        return ResponseEntity.ok(
                ApiResponse.ok("Cita actualizada correctamente", citaMapper.toResponse(actualizada)));
    }

    @Operation(summary = "Eliminar una cita")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Cita eliminada correctamente", null));
    }
}
