package com.vet.animalink.historico_service.controller;

import com.vet.animalink.historico_service.assembler.HistoricoModelAssembler;
import com.vet.animalink.historico_service.dto.ApiResponse;
import com.vet.animalink.historico_service.dto.HistoricoRequest;
import com.vet.animalink.historico_service.dto.HistoricoResponse;
import com.vet.animalink.historico_service.mapper.HistoricoMapper;
import com.vet.animalink.historico_service.model.Historico;
import com.vet.animalink.historico_service.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Históricos", description = "Historial, auditoría y registro de ventas pasadas")
@RestController
@RequestMapping("/api/v1/historicos")
public class HistoricoController {

    private final HistoricoService historicoService;
    private final HistoricoMapper historicoMapper;
    private final HistoricoModelAssembler assembler;

    public HistoricoController(HistoricoService historicoService, 
                               HistoricoMapper historicoMapper,
                               HistoricoModelAssembler assembler) {
        this.historicoService = historicoService;
        this.historicoMapper = historicoMapper;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los registros históricos (con enlaces HATEOAS)")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<HistoricoResponse>>>> listar() {
        List<HistoricoResponse> lista = historicoService.listar().stream()
                .map(historicoMapper::toResponse)
                .toList();
        String msg = lista.isEmpty() ? "No hay históricos registrados" : "Se encontraron " + lista.size() + " histórico(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, assembler.toCollection(lista)));
    }

    @Operation(summary = "Obtener un registro histórico por su ID (con enlaces HATEOAS)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<HistoricoResponse>>> obtener(@PathVariable Long id) {
        Historico historico = historicoService.obtenerPorId(id);
        HistoricoResponse data = historicoMapper.toResponse(historico);
        return ResponseEntity.ok(ApiResponse.ok("Histórico encontrado correctamente", assembler.toModel(data)));
    }

    @Operation(summary = "Registrar un nuevo histórico de venta")
    @PostMapping
    public ResponseEntity<ApiResponse<HistoricoResponse>> registrar(@Valid @RequestBody HistoricoRequest request) {
        Historico creado = historicoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Histórico registrado correctamente", historicoMapper.toResponse(creado)));
    }

    @Operation(summary = "Eliminar un registro histórico")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        historicoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Histórico eliminado correctamente", null));
    }
}