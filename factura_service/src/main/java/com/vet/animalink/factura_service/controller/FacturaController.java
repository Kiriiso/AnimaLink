package com.vet.animalink.factura_service.controller;

import com.vet.animalink.factura_service.dto.ApiResponse;
import com.vet.animalink.factura_service.dto.FacturaRequest;
import com.vet.animalink.factura_service.dto.FacturaResponse;
import com.vet.animalink.factura_service.mapper.FacturaMapper;
import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facturas", description = "Emisión y gestión de facturas")
@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaMapper facturaMapper;

    public FacturaController(FacturaService facturaService, FacturaMapper facturaMapper) {
        this.facturaService = facturaService;
        this.facturaMapper = facturaMapper;
    }

    @Operation(summary = "Listar todas las facturas")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FacturaResponse>>> listar() {
        List<FacturaResponse> data = facturaService.listar().stream()
                .map(facturaMapper::toResponse)
                .toList();
        String msg = data.isEmpty() ? "No hay facturas registradas" : "Se encontraron " + data.size() + " factura(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, data));
    }

    @Operation(summary = "Obtener una factura por su ID (incluye nombre del cliente)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacturaResponse>> obtener(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        FacturaResponse data = facturaMapper.toResponse(
                factura, facturaService.obtenerCliente(factura.getClienteId()).orElse(null));
        return ResponseEntity.ok(ApiResponse.ok("Factura encontrada correctamente", data));
    }

    @Operation(summary = "Emitir una factura (valida cliente, toma precios de inventario y descuenta stock)")
    @PostMapping
    public ResponseEntity<ApiResponse<FacturaResponse>> emitir(@Valid @RequestBody FacturaRequest request) {
        Factura creada = facturaService.emitir(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Factura emitida correctamente", facturaMapper.toResponse(creada)));
    }

    @Operation(summary = "Eliminar una factura")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        facturaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Factura eliminada correctamente", null));
    }
}
