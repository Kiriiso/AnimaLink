package com.vet.animalink.historial_service.controller;

import com.vet.animalink.historial_service.dto.ApiResponse;
import com.vet.animalink.historial_service.dto.HistorialResponse;
import com.vet.animalink.historial_service.exception.ResourceNotFoundException;
import com.vet.animalink.historial_service.mapper.HistorialMapper;
import com.vet.animalink.historial_service.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Historial V2", description = "Consultas avanzadas del historial clínico")
@RestController
@RequestMapping("/api/v2/historiales")
public class HistorialControllerV2 {

    private final HistorialService historialService;
    private final HistorialMapper historialMapper;

    public HistorialControllerV2(HistorialService historialService, HistorialMapper historialMapper) {
        this.historialService = historialService;
        this.historialMapper = historialMapper;
    }

    @Operation(summary = "Obtener el historial completo de una mascota (más reciente primero)")
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<ApiResponse<List<HistorialResponse>>> porMascota(@PathVariable Long mascotaId) {
        List<HistorialResponse> data = historialService.listarPorMascota(mascotaId).stream()
                .map(historialMapper::toResponse)
                .toList();
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("La mascota " + mascotaId + " no tiene historial registrado");
        }
        return ResponseEntity.ok(ApiResponse.ok("Historial de la mascota " + mascotaId + ": " + data.size(), data));
    }

    @Operation(summary = "Buscar entradas por diagnóstico")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<HistorialResponse>>> buscar(@RequestParam String diagnostico) {
        List<HistorialResponse> data = historialService.buscarPorDiagnostico(diagnostico).stream()
                .map(historialMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Coincidencias: " + data.size(), data));
    }
}
