package com.vet.animalink.factura_service.mapper;

import com.vet.animalink.factura_service.client.dto.ClienteDTO;
import com.vet.animalink.factura_service.dto.FacturaResponse;
import com.vet.animalink.factura_service.model.Factura;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacturaMapper {

    public FacturaResponse toResponse(Factura f) {
        return toResponse(f, null);
    }

    public FacturaResponse toResponse(Factura f, ClienteDTO cliente) {
        List<FacturaResponse.ItemResponse> items = f.getDetalles().stream()
                .map(d -> new FacturaResponse.ItemResponse(
                        d.getInsumoId(),
                        d.getDescripcion(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()))
                .toList();

        return new FacturaResponse(
                f.getId(),
                f.getClienteId(),
                cliente != null ? cliente.nombreCompleto() : null,
                f.getFecha(),
                f.getEstado() != null ? f.getEstado().name() : null,
                f.getTotal(),
                items
        );
    }
}
