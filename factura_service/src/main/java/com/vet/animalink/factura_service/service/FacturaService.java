package com.vet.animalink.factura_service.service;

import com.vet.animalink.factura_service.client.ClienteClient;
import com.vet.animalink.factura_service.client.InsumoClient;
import com.vet.animalink.factura_service.client.dto.ClienteDTO;
import com.vet.animalink.factura_service.client.dto.InsumoDTO;
import com.vet.animalink.factura_service.dto.FacturaRequest;
import com.vet.animalink.factura_service.exception.ResourceNotFoundException;
import com.vet.animalink.factura_service.model.DetalleFactura;
import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.model.enums.EstadoFactura;
import com.vet.animalink.factura_service.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteClient clienteClient;
    private final InsumoClient insumoClient;

    public FacturaService(FacturaRepository facturaRepository,
                          ClienteClient clienteClient,
                          InsumoClient insumoClient) {
        this.facturaRepository = facturaRepository;
        this.clienteClient = clienteClient;
        this.insumoClient = insumoClient;
    }

    public List<Factura> listar() {
        return facturaRepository.findAll();
    }

    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con id " + id));
    }

    /**
     * Emite una factura: valida el cliente, toma precios desde inventario_service,
     * calcula el total y descuenta el stock de cada insumo.
     */
    @Transactional
    public Factura emitir(FacturaRequest request) {
        // 1) El cliente debe existir (cliente_service)
        clienteClient.obtenerCliente(request.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede facturar: el cliente con id " + request.clienteId() + " no existe"));

        Factura factura = Factura.builder()
                .clienteId(request.clienteId())
                .estado(EstadoFactura.EMITIDA)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        // 2) Por cada ítem: tomar precio desde inventario_service y descontar stock
        for (FacturaRequest.ItemRequest item : request.items()) {
            InsumoDTO insumo = insumoClient.obtenerInsumo(item.insumoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "El insumo con id " + item.insumoId() + " no existe en el inventario"));

            BigDecimal precioUnitario = insumo.precioVenta();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(item.cantidad()));

            DetalleFactura detalle = DetalleFactura.builder()
                    .insumoId(insumo.id())
                    .descripcion(insumo.nombre())
                    .cantidad(item.cantidad())
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .build();
            factura.agregarDetalle(detalle);

            // Descontar stock en inventario_service (escritura entre microservicios)
            insumoClient.descontarStock(item.insumoId(), item.cantidad());

            total = total.add(subtotal);
        }

        factura.setTotal(total);
        return facturaRepository.save(factura);
    }

    @Transactional
    public Factura cambiarEstado(Long id, EstadoFactura estado) {
        Factura factura = obtenerPorId(id);
        factura.setEstado(estado);
        return facturaRepository.save(factura);
    }

    @Transactional
    public void eliminar(Long id) {
        Factura factura = obtenerPorId(id);
        facturaRepository.delete(factura);
    }

    public Optional<ClienteDTO> obtenerCliente(Long clienteId) {
        return clienteClient.obtenerCliente(clienteId);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Factura> listarPorCliente(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId);
    }

    public List<Factura> listarPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado);
    }
}
