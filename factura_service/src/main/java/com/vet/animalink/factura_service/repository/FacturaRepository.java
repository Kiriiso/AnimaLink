package com.vet.animalink.factura_service.repository;

import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.model.enums.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByClienteId(Long clienteId);

    List<Factura> findByEstado(EstadoFactura estado);
}
