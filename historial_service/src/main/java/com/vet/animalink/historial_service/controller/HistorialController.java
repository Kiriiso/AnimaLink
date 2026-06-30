package com.vet.animalink.historial_service.controller;

import com.vet.animalink.historial_service.dto.ApiResponse;
import com.vet.animalink.historial_service.dto.HistorialRequest;
import com.vet.animalink.historial_service.dto.HistorialResponse;
import com.vet.animalink.historial_service.mapper.HistorialMapper;
import com.vet.animalink.historial_service.model.Historial;
import com.vet.animalink.historial_service.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Historial clínico", description = "Gestión del historial clínico de las mascotas")
@RestController
@RequestMapping("/api/v1/historiales")
public class HistorialController {

    private final HistorialService historialService;
    private final HistorialMapper historialMapper;

    public HistorialController(HistorialService historialService, HistorialMapper historialMapper) {
        this.historialService = historialService;
        this.historialMapper = historialMapper;
    }

    @Operation(summary = "Listar todas las entradas del historial")
    @GetMapping
    public ResponseEntity<ApiResponse<List<HistorialResponse>>> listar() {
        List<HistorialResponse> data = historialService.listar().stream()
                .map(historialMapper::toResponse)
                .toList();
        String msg = data.isEmpty() ? "No hay historiales registrados" : "Se encontraron " + data.size() + " registro(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, data));
    }

    @Operation(summary = "Obtener una entrada del historial por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HistorialResponse>> obtener(@PathVariable Long id) {
        Historial historial = historialService.obtenerPorId(id);
        HistorialResponse data = historialMapper.toResponse(
                historial, historialService.obtenerMascota(historial.getMascotaId()).orElse(null));
        return ResponseEntity.ok(ApiResponse.ok("Historial encontrado correctamente", data));
    }

    @Operation(summary = "Registrar una entrada del historial (valida que la mascota exista)")
    @PostMapping
    public ResponseEntity<ApiResponse<HistorialResponse>> crear(@Valid @RequestBody HistorialRequest request) {
        Historial creado = historialService.crear(historialMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Historial registrado correctamente", historialMapper.toResponse(creado)));
    }

    @Operation(summary = "Actualizar una entrada del historial")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HistorialResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HistorialRequest request) {
        Historial actualizado = historialService.actualizar(id, historialMapper.toEntity(request));
        return ResponseEntity.ok(
                ApiResponse.ok("Historial actualizado correctamente", historialMapper.toResponse(actualizado)));
    }

    @Operation(summary = "Eliminar una entrada del historial")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        historialService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Historial eliminado correctamente", null));
    }
}
