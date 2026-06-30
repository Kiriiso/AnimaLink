package com.vet.animalink.control_alta_service.controller;

import com.vet.animalink.control_alta_service.dto.ApiResponse;
import com.vet.animalink.control_alta_service.dto.ControlAltaRequest;
import com.vet.animalink.control_alta_service.dto.ControlAltaResponse;
import com.vet.animalink.control_alta_service.mapper.ControlAltaMapper;
import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.service.ControlAltaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Control de hospitalización", description = "Ingreso y alta de mascotas hospitalizadas")
@RestController
@RequestMapping("/api/v1/controles")
public class ControlAltaController {

    private final ControlAltaService controlAltaService;
    private final ControlAltaMapper controlAltaMapper;

    public ControlAltaController(ControlAltaService controlAltaService, ControlAltaMapper controlAltaMapper) {
        this.controlAltaService = controlAltaService;
        this.controlAltaMapper = controlAltaMapper;
    }

    @Operation(summary = "Listar todos los controles de hospitalización")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ControlAltaResponse>>> listar() {
        List<ControlAltaResponse> data = controlAltaService.listar().stream()
                .map(controlAltaMapper::toResponse)
                .toList();
        String msg = data.isEmpty() ? "No hay controles registrados" : "Se encontraron " + data.size() + " control(es)";
        return ResponseEntity.ok(ApiResponse.ok(msg, data));
    }

    @Operation(summary = "Obtener un control por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ControlAltaResponse>> obtener(@PathVariable Long id) {
        ControlAlta control = controlAltaService.obtenerPorId(id);
        ControlAltaResponse data = controlAltaMapper.toResponse(
                control, controlAltaService.obtenerMascota(control.getMascotaId()).orElse(null));
        return ResponseEntity.ok(ApiResponse.ok("Control encontrado correctamente", data));
    }

    @Operation(summary = "Registrar el ingreso de una mascota (valida que exista)")
    @PostMapping
    public ResponseEntity<ApiResponse<ControlAltaResponse>> registrarIngreso(
            @Valid @RequestBody ControlAltaRequest request) {
        ControlAlta creado = controlAltaService.registrarIngreso(controlAltaMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Ingreso registrado correctamente", controlAltaMapper.toResponse(creado)));
    }

    @Operation(summary = "Dar de alta una mascota hospitalizada (operación personalizada)")
    @PatchMapping("/{id}/alta")
    public ResponseEntity<ApiResponse<ControlAltaResponse>> darDeAlta(
            @PathVariable Long id,
            @RequestParam(required = false) String observaciones) {
        ControlAlta dado = controlAltaService.darDeAlta(id, observaciones);
        return ResponseEntity.ok(ApiResponse.ok("Mascota dada de alta correctamente", controlAltaMapper.toResponse(dado)));
    }

    @Operation(summary = "Eliminar un control")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        controlAltaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Control eliminado correctamente", null));
    }
}
