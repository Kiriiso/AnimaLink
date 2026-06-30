package com.vet.animalink.factura_service.controller;

import com.vet.animalink.factura_service.dto.ApiResponse;
import com.vet.animalink.factura_service.dto.FacturaResponse;
import com.vet.animalink.factura_service.mapper.FacturaMapper;
import com.vet.animalink.factura_service.model.enums.EstadoFactura;
import com.vet.animalink.factura_service.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facturas V2", description = "Filtros avanzados y cambio de estado de facturas")
@RestController
@RequestMapping("/api/v2/facturas")
public class FacturaControllerV2 {

    private final FacturaService facturaService;
    private final FacturaMapper facturaMapper;

    public FacturaControllerV2(FacturaService facturaService, FacturaMapper facturaMapper) {
        this.facturaService = facturaService;
        this.facturaMapper = facturaMapper;
    }

    @Operation(summary = "Listar facturas de un cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<FacturaResponse>>> porCliente(@PathVariable Long clienteId) {
        List<FacturaResponse> data = facturaService.listarPorCliente(clienteId).stream()
                .map(facturaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Facturas del cliente " + clienteId + ": " + data.size(), data));
    }

    @Operation(summary = "Listar facturas por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<FacturaResponse>>> porEstado(@PathVariable EstadoFactura estado) {
        List<FacturaResponse> data = facturaService.listarPorEstado(estado).stream()
                .map(facturaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok("Facturas en estado " + estado + ": " + data.size(), data));
    }

    @Operation(summary = "Cambiar el estado de una factura (ej: marcar como PAGADA)")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<FacturaResponse>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoFactura estado) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Estado actualizado a " + estado,
                facturaMapper.toResponse(facturaService.cambiarEstado(id, estado))));
    }
}
