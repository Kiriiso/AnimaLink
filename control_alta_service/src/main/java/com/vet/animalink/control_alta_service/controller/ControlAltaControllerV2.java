package com.vet.animalink.control_alta_service.controller;

import com.vet.animalink.control_alta_service.dto.ApiResponse;
import com.vet.animalink.control_alta_service.dto.ControlAltaResponse;
import com.vet.animalink.control_alta_service.mapper.ControlAltaMapper;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import com.vet.animalink.control_alta_service.service.ControlAltaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Control V2", description = "Consultas avanzadas de hospitalización")
@RestController
@RequestMapping("/api/v2/controles")
public class ControlAltaControllerV2 {

    private final ControlAltaService controlAltaService;
    private final ControlAltaMapper controlAltaMapper;

    public ControlAltaControllerV2(ControlAltaService controlAltaService, ControlAltaMapper controlAltaMapper) {
        this.controlAltaService = controlAltaService;
        this.controlAltaMapper = controlAltaMapper;
    }

    @Operation(summary = "Listar controles por estado (HOSPITALIZADO / DADO_DE_ALTA)")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<ControlAltaResponse>>> porEstado(@PathVariable EstadoControl estado) {
        List<ControlAltaResponse> data = controlAltaService.listarPorEstado(estado).stream()
                .map(controlAltaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Controles en estado " + estado + ": " + data.size(), data));
    }

    @Operation(summary = "Listar el historial de hospitalizaciones de una mascota")
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<ApiResponse<List<ControlAltaResponse>>> porMascota(@PathVariable Long mascotaId) {
        List<ControlAltaResponse> data = controlAltaService.listarPorMascota(mascotaId).stream()
                .map(controlAltaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Hospitalizaciones de la mascota " + mascotaId + ": " + data.size(), data));
    }
}
