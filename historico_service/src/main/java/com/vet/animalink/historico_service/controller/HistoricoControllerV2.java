package com.vet.animalink.historico_service.controller;

import com.vet.animalink.historico_service.dto.ApiResponse;
import com.vet.animalink.historico_service.dto.HistoricoResponse;
import com.vet.animalink.historico_service.mapper.HistoricoMapper;
import com.vet.animalink.historico_service.service.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Históricos V2", description = "Filtros avanzados para registros históricos")
@RestController
@RequestMapping("/api/v2/historicos")
public class HistoricoControllerV2 {

    private final HistoricoService historicoService;
    private final HistoricoMapper historicoMapper;

    public HistoricoControllerV2(HistoricoService historicoService, HistoricoMapper historicoMapper) {
        this.historicoService = historicoService;
        this.historicoMapper = historicoMapper;
    }

    @Operation(summary = "Listar históricos por método de pago (ej: EFECTIVO, TARJETA)")
    @GetMapping("/metodo-pago/{metodoPago}")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> porMetodoPago(@PathVariable String metodoPago) {
        List<HistoricoResponse> data = historicoService.listarPorMetodoPago(metodoPago).stream()
                .map(historicoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(
                "Registros con método de pago " + metodoPago + ": " + data.size(), data));
    }

    @Operation(summary = "Listar históricos por rango de fechas")
    @GetMapping("/fechas")
    public ResponseEntity<ApiResponse<List<HistoricoResponse>>> porRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        List<HistoricoResponse> data = historicoService.listarPorRangoFechas(inicio, fin).stream()
                .map(historicoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(
                "Registros encontrados entre las fechas especificadas: " + data.size(), data));
    }
}