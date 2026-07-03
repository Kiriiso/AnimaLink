package com.vet.animalink.historico_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoDTO {
    private Long id;
    private int totalNeto;
    private int descuento;
    private int iva;
    private int totalVenta;
    private String metodoPago;
    private Date fechaVenta;

}
