package com.vet.animalink.historico_service.mapper;

import com.vet.animalink.historico_service.dto.HistoricoResponse;
import com.vet.animalink.historico_service.model.Historico;
import org.springframework.stereotype.Component;

@Component
public class HistoricoMapper {

    public HistoricoResponse toResponse(Historico historico) {
        if (historico == null) {
            return null;
        }
        return new HistoricoResponse(
                historico.getId(),
                historico.getTotalNeto(),
                historico.getDescuento(),
                historico.getIva(),
                historico.getTotalVenta(),
                historico.getMetodoPago(),
                historico.getFechaVenta()
        );
    }
}