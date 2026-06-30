package com.vet.animalink.cliente_service.controller;

import com.vet.animalink.cliente_service.assembler.ClienteModelAssembler;
import com.vet.animalink.cliente_service.dto.ApiResponse;
import com.vet.animalink.cliente_service.dto.ClienteRequest;
import com.vet.animalink.cliente_service.dto.ClienteResponse;
import com.vet.animalink.cliente_service.mapper.ClienteMapper;
import com.vet.animalink.cliente_service.model.Cliente;
import com.vet.animalink.cliente_service.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST principal (CRUD) del microservicio de clientes.
 * Todas las respuestas usan el envoltorio {@link ApiResponse} con mensajes personalizados.
 */
@Tag(name = "Clientes", description = "Gestión de clientes (dueños de mascotas)")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;
    private final ClienteModelAssembler assembler;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper,
                             ClienteModelAssembler assembler) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los clientes (con enlaces HATEOAS)")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<ClienteResponse>>>> listar() {
        List<ClienteResponse> lista = clienteService.listar().stream()
                .map(clienteMapper::toResponse)
                .toList();
        String msg = lista.isEmpty() ? "No hay clientes registrados" : "Se encontraron " + lista.size() + " cliente(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, assembler.toCollection(lista)));
    }

    @Operation(summary = "Obtener un cliente por su ID (con enlaces HATEOAS)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<ClienteResponse>>> obtener(@PathVariable Long id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        EntityModel<ClienteResponse> model = assembler.toModel(clienteMapper.toResponse(cliente));
        return ResponseEntity.ok(ApiResponse.ok("Cliente encontrado correctamente", model));
    }

    @Operation(summary = "Crear un nuevo cliente")
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> crear(@Valid @RequestBody ClienteRequest request) {
        Cliente creado = clienteService.crear(clienteMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Cliente creado correctamente", clienteMapper.toResponse(creado)));
    }

    @Operation(summary = "Actualizar un cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request) {
        Cliente actualizado = clienteService.actualizar(id, clienteMapper.toEntity(request));
        return ResponseEntity.ok(
                ApiResponse.ok("Cliente actualizado correctamente", clienteMapper.toResponse(actualizado)));
    }

    @Operation(summary = "Eliminar un cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Cliente eliminado correctamente", null));
    }
}
