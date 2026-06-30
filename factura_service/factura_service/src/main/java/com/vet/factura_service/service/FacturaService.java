package com.vet.factura_service.service;

import com.vet.factura_service.model.Factura;
import java.util.List;

public interface FacturaService {

    Factura save(Factura entity);

    Factura update(Long id, Factura entity);

    Factura findById(Long id);

    List<Factura> findAll();

    void deleteById(Long id);
}
