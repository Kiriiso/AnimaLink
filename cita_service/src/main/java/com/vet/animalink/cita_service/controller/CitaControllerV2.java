package com.vet.animalink.cita_service.controller;

import com.vet.animalink.cita_service.dto.ApiResponse;
import com.vet.animalink.cita_service.dto.CitaResponse;
import com.vet.animalink.cita_service.mapper.CitaMapper;
import com.vet.animalink.cita_service.model.enums.EstadoCita;
import com.vet.animalink.cita_service.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Citas V2", description = "Filtros avanzados y cambio de estado de citas")
@RestController
@RequestMapping("/api/v2/citas")
public class CitaControllerV2 {

    private final CitaService citaService;
    private final CitaMapper citaMapper;

    public CitaControllerV2(CitaService citaService, CitaMapper citaMapper) {
        this.citaService = citaService;
        this.citaMapper = citaMapper;
    }

    @Operation(summary = "Listar citas de una mascota")
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> porMascota(@PathVariable Long mascotaId) {
        List<CitaResponse> data = citaService.listarPorMascota(mascotaId).stream()
                .map(citaMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.ok("Citas de la mascota " + mascotaId + ": " + data.size(), data));
    }

    @Operation(summary = "Listar citas de un veterinario")
    @GetMapping("/veterinario/{veterinarioId}")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> porVeterinario(@PathVariable Long veterinarioId) {
        List<CitaResponse> data = citaService.listarPorVeterinario(veterinarioId).stream()
                .map(citaMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.ok("Citas del veterinario " + veterinarioId + ": " + data.size(), data));
    }

    @Operation(summary = "Listar citas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<CitaResponse>>> porEstado(@PathVariable EstadoCita estado) {
        List<CitaResponse> data = citaService.listarPorEstado(estado).stream()
                .map(citaMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.ok("Citas en estado " + estado + ": " + data.size(), data));
    }

    @Operation(summary = "Cambiar el estado de una cita (operación personalizada)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<CitaResponse>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoCita estado) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Estado actualizado a " + estado,
                citaMapper.toResponse(citaService.cambiarEstado(id, estado))));
    }
}
