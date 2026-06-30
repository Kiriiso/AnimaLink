package com.vet.animalink.inventario_service.mapper;

import com.vet.animalink.inventario_service.dto.InsumoRequest;
import com.vet.animalink.inventario_service.dto.InsumoResponse;
import com.vet.animalink.inventario_service.model.Insumo;
import org.springframework.stereotype.Component;

@Component
public class InsumoMapper {

    public InsumoResponse toResponse(Insumo i) {
        boolean bajoStock = i.getStock() != null && i.getStockMinimo() != null
                && i.getStock() <= i.getStockMinimo();
        return new InsumoResponse(
                i.getId(),
                i.getNombre(),
                i.getCategoria() != null ? i.getCategoria().name() : null,
                i.getDescripcion(),
                i.getStock(),
                i.getStockMinimo(),
                i.getPrecioVenta(),     // NO se expone precioCosto
                i.getFechaVencimiento(),
                bajoStock
        );
    }

    public Insumo toEntity(InsumoRequest req) {
        return Insumo.builder()
                .nombre(req.nombre())
                .categoria(req.categoria())
                .descripcion(req.descripcion())
                .stock(req.stock())
                .stockMinimo(req.stockMinimo() != null ? req.stockMinimo() : 5)
                .precioCosto(req.precioCosto())
                .precioVenta(req.precioVenta())
                .fechaVencimiento(req.fechaVencimiento())
                .activo(true)
                .build();
    }
}
