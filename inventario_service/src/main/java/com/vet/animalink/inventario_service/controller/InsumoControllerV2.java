package com.vet.animalink.inventario_service.controller;

import com.vet.animalink.inventario_service.dto.ApiResponse;
import com.vet.animalink.inventario_service.dto.InsumoResponse;
import com.vet.animalink.inventario_service.mapper.InsumoMapper;
import com.vet.animalink.inventario_service.model.enums.Categoria;
import com.vet.animalink.inventario_service.service.InsumoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventario V2", description = "Consultas avanzadas del inventario")
@RestController
@RequestMapping("/api/v2/insumos")
public class InsumoControllerV2 {

    private final InsumoService insumoService;
    private final InsumoMapper insumoMapper;

    public InsumoControllerV2(InsumoService insumoService, InsumoMapper insumoMapper) {
        this.insumoService = insumoService;
        this.insumoMapper = insumoMapper;
    }

    @Operation(summary = "Listar insumos por categoría")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse<List<InsumoResponse>>> porCategoria(@PathVariable Categoria categoria) {
        List<InsumoResponse> data = insumoService.listarPorCategoria(categoria).stream()
                .map(insumoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Insumos de categoría " + categoria + ": " + data.size(), data));
    }

    @Operation(summary = "Listar insumos con stock bajo el mínimo (alerta de reposición)")
    @GetMapping("/bajo-stock")
    public ResponseEntity<ApiResponse<List<InsumoResponse>>> bajoStock() {
        List<InsumoResponse> data = insumoService.listarBajoStock().stream()
                .map(insumoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Insumos bajo stock: " + data.size(), data));
    }
}
