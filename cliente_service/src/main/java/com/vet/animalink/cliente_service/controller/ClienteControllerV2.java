package com.vet.animalink.cliente_service.controller;

import com.vet.animalink.cliente_service.dto.ApiResponse;
import com.vet.animalink.cliente_service.dto.ClienteResponse;
import com.vet.animalink.cliente_service.exception.ResourceNotFoundException;
import com.vet.animalink.cliente_service.mapper.ClienteMapper;
import com.vet.animalink.cliente_service.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Operaciones personalizadas / búsquedas avanzadas (más allá del CRUD básico).
 */
@Tag(name = "Clientes V2", description = "Búsquedas y filtros avanzados de clientes")
@RestController
@RequestMapping("/api/v2/clientes")
public class ClienteControllerV2 {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteControllerV2(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @Operation(summary = "Buscar clientes por nombre o apellido")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> buscar(@RequestParam String texto) {
        List<ClienteResponse> data = clienteService.buscarPorNombre(texto).stream()
                .map(clienteMapper::toResponse)
                .toList();
        if (data.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron clientes que coincidan con '" + texto + "'");
        }
        return ResponseEntity.ok(ApiResponse.ok("Se encontraron " + data.size() + " coincidencia(s)", data));
    }

    @Operation(summary = "Listar solo los clientes activos")
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> activos() {
        List<ClienteResponse> data = clienteService.listarActivos().stream()
                .map(clienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Clientes activos: " + data.size(), data));
    }
}
