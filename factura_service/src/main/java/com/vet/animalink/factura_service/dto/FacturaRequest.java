package com.vet.animalink.factura_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(description = "Datos para emitir una factura")
public record FacturaRequest(

        @Schema(example = "3")
        @NotNull(message = "El clienteId es obligatorio")
        @Positive(message = "El clienteId debe ser positivo")
        Long clienteId,

        @Schema(description = "Líneas de la factura")
        @NotEmpty(message = "La factura debe tener al menos un ítem")
        @Valid
        List<ItemRequest> items
) {
    @Schema(description = "Ítem de la factura")
    public record ItemRequest(

            @Schema(example = "4")
            @NotNull(message = "El insumoId es obligatorio")
            @Positive(message = "El insumoId debe ser positivo")
            Long insumoId,

            @Schema(example = "2")
            @NotNull(message = "La cantidad es obligatoria")
            @Positive(message = "La cantidad debe ser positiva")
            Integer cantidad
    ) {}
}
