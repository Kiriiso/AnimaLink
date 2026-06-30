package com.vet.animalink.mascota_service.controller;

import com.vet.animalink.mascota_service.dto.ApiResponse;
import com.vet.animalink.mascota_service.dto.MascotaResponse;
import com.vet.animalink.mascota_service.exception.ResourceNotFoundException;
import com.vet.animalink.mascota_service.mapper.MascotaMapper;
import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mascotas V2", description = "Búsquedas y filtros avanzados de mascotas")
@RestController
@RequestMapping("/api/v2/mascotas")
public class MascotaControllerV2 {

    private final MascotaService mascotaService;
    private final MascotaMapper mascotaMapper;

    public MascotaControllerV2(MascotaService mascotaService, MascotaMapper mascotaMapper) {
        this.mascotaService = mascotaService;
        this.mascotaMapper = mascotaMapper;
    }

    @Operation(summary = "Listar mascotas de un cliente (dueño)")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<MascotaResponse>>> porCliente(@PathVariable Long clienteId) {
        List<MascotaResponse> data = mascotaService.listarPorCliente(clienteId).stream()
                .map(mascotaMapper::toResponse)
                .toList();
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("El cliente " + clienteId + " no tiene mascotas registradas");
        }
        return ResponseEntity.ok(ApiResponse.ok("Mascotas del cliente " + clienteId + ": " + data.size(), data));
    }

    @Operation(summary = "Listar mascotas por especie")
    @GetMapping("/especie/{especie}")
    public ResponseEntity<ApiResponse<List<MascotaResponse>>> porEspecie(@PathVariable Especie especie) {
        List<MascotaResponse> data = mascotaService.listarPorEspecie(especie).stream()
                .map(mascotaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Mascotas de especie " + especie + ": " + data.size(), data));
    }
}
