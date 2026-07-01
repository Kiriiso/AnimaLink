package com.vet.animalink.factura_service.controller;

import com.vet.animalink.factura_service.assembler.FacturaModelAssembler;
import com.vet.animalink.factura_service.dto.ApiResponse;
import com.vet.animalink.factura_service.dto.FacturaRequest;
import com.vet.animalink.factura_service.dto.FacturaResponse;
import com.vet.animalink.factura_service.mapper.FacturaMapper;
import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private final FacturaModelAssembler assembler;

    public FacturaController(FacturaService facturaService, FacturaMapper facturaMapper,
                             FacturaModelAssembler assembler) {
        this.facturaService = facturaService;
        this.facturaMapper = facturaMapper;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todas las facturas (con enlaces HATEOAS)")
    @GetMapping
    public ResponseEntity<ApiResponse<CollectionModel<EntityModel<FacturaResponse>>>> listar() {
        List<FacturaResponse> lista = facturaService.listar().stream()
                .map(facturaMapper::toResponse)
                .toList();
        String msg = lista.isEmpty() ? "No hay facturas registradas" : "Se encontraron " + lista.size() + " factura(s)";
        return ResponseEntity.ok(ApiResponse.ok(msg, assembler.toCollection(lista)));
    }

    @Operation(summary = "Obtener una factura por su ID (incluye nombre del cliente y enlaces HATEOAS)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EntityModel<FacturaResponse>>> obtener(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        FacturaResponse data = facturaMapper.toResponse(
                factura, facturaService.obtenerCliente(factura.getClienteId()).orElse(null));
        return ResponseEntity.ok(ApiResponse.ok("Factura encontrada correctamente", assembler.toModel(data)));
    }

    @Operation(summary = "Emitir una factura (valida cliente, toma precios de inventario y descuenta stock)")
    @PostMapping
    public ResponseEntity<ApiResponse<FacturaResponse>> emitir(
            @Valid @RequestBody FacturaRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Factura creada = facturaService.emitir(request, authorizationHeader);
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
