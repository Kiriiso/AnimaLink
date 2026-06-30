package com.vet.animalink.inventario_service.service;

import com.vet.animalink.inventario_service.exception.ResourceNotFoundException;
import com.vet.animalink.inventario_service.model.Insumo;
import com.vet.animalink.inventario_service.model.enums.Categoria;
import com.vet.animalink.inventario_service.repository.InsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public List<Insumo> listar() {
        return insumoRepository.findAll();
    }

    public Insumo obtenerPorId(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id " + id));
    }

    @Transactional
    public Insumo crear(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    @Transactional
    public Insumo actualizar(Long id, Insumo datos) {
        Insumo existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setCategoria(datos.getCategoria());
        existente.setDescripcion(datos.getDescripcion());
        existente.setStock(datos.getStock());
        existente.setStockMinimo(datos.getStockMinimo());
        existente.setPrecioCosto(datos.getPrecioCosto());
        existente.setPrecioVenta(datos.getPrecioVenta());
        existente.setFechaVencimiento(datos.getFechaVencimiento());
        return insumoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Insumo existente = obtenerPorId(id);
        insumoRepository.delete(existente);
    }

    /**
     * Ajusta el stock sumando 'cantidad' (puede ser negativa para descontar).
     * Lo usa, por ejemplo, factura_service al vender insumos.
     */
    @Transactional
    public Insumo ajustarStock(Long id, int cantidad) {
        Insumo insumo = obtenerPorId(id);
        int nuevoStock = insumo.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el insumo " + insumo.getNombre()
                            + " (stock actual: " + insumo.getStock() + ", solicitado: " + (-cantidad) + ")");
        }
        insumo.setStock(nuevoStock);
        return insumoRepository.save(insumo);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Insumo> listarPorCategoria(Categoria categoria) {
        return insumoRepository.findByCategoria(categoria);
    }

    public List<Insumo> listarBajoStock() {
        return insumoRepository.findBajoStock();
    }
}
