package com.vet.animalink.inventario_service.controller;

import com.vet.animalink.inventario_service.assembler.InsumoModelAssembler;
import com.vet.animalink.inventario_service.dto.ApiResponse;
import com.vet.animalink.inventario_service.dto.InsumoRequest;
import com.vet.animalink.inventario_service.dto.InsumoResponse;
import com.vet.animalink.inventario_service.mapper.InsumoMapper;
import com.vet.animalink.inventario_service.model.Insumo;
import com.vet.animalink.inventario_service.service.InsumoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventario", description = "Gestión de insumos y medicamentos")
@RestController
@RequestMapping("/api/v1/insumos")
public class InsumoController {

    private final InsumoService insumoService;
    private final InsumoMapper insumoMapper;
    private final InsumoModelAssembler assembler;

    public InsumoController(InsumoService insumoService, InsumoMapper insumoMapper,
                            InsumoModelAssembler assembler) {
        this.insumoService = insumoService;
        this.insumoMapper = insumoMapper;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los insumos (con enlaces HATEOAS)")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<InsumoResponse>>>> listar() {
        List<InsumoResponse> lista = insumoService.listar().stream()
                .map(insumoMapper::toResponse)
                .toList();
        String msg = lista.isEmpty() ? "No hay insumos registrados" : "Se encontraron " + lista.size() + " insumo(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, assembler.toCollection(lista)));
    }

    @Operation(summary = "Obtener un insumo por su ID (con enlaces HATEOAS)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<InsumoResponse>>> obtener(@PathVariable Long id) {
        Insumo insumo = insumoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok("Insumo encontrado correctamente", assembler.toModel(insumoMapper.toResponse(insumo))));
    }

    @Operation(summary = "Crear un nuevo insumo")
    @PostMapping
    public ResponseEntity<ApiResponse<InsumoResponse>> crear(@Valid @RequestBody InsumoRequest request) {
        Insumo creado = insumoService.crear(insumoMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Insumo creado correctamente", insumoMapper.toResponse(creado)));
    }

    @Operation(summary = "Actualizar un insumo existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsumoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InsumoRequest request) {
        Insumo actualizado = insumoService.actualizar(id, insumoMapper.toEntity(request));
        return ResponseEntity.ok(ApiResponse.ok("Insumo actualizado correctamente", insumoMapper.toResponse(actualizado)));
    }

    @Operation(summary = "Ajustar el stock de un insumo (cantidad positiva suma, negativa descuenta)")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<InsumoResponse>> ajustarStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {
        Insumo ajustado = insumoService.ajustarStock(id, cantidad);
        return ResponseEntity.ok(ApiResponse.ok("Stock actualizado correctamente", insumoMapper.toResponse(ajustado)));
    }

    @Operation(summary = "Eliminar un insumo")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        insumoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Insumo eliminado correctamente", null));
    }
}
