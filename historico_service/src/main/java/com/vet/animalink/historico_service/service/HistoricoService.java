package com.vet.animalink.historico_service.service;

import com.vet.animalink.historico_service.dto.HistoricoRequest;
import com.vet.animalink.historico_service.exception.ResourceNotFoundException;
import com.vet.animalink.historico_service.model.Historico;
import com.vet.animalink.historico_service.repository.HistoricoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private static final Logger logger = LoggerFactory.getLogger(HistoricoService.class);

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    /**
     * Registra un nuevo registro histórico en el sistema.
     */
    @Transactional
    public Historico registrar(HistoricoRequest request) {
        logger.info("Registrando nuevo histórico de venta por método: {}", request.metodoPago());
        
        Historico historico = Historico.builder()
                .totalNeto(request.totalNeto())
                .descuento(request.descuento())
                .iva(request.iva())
                .totalVenta(request.totalVenta())
                .metodoPago(request.metodoPago())
                .fechaVenta(request.fechaVenta())
                .build();

        return historicoRepository.save(historico);
    }

    /**
     * Obtiene todos los registros históricos (V1).
     */
    @Transactional(readOnly = true)
    public List<Historico> listar() {
        return historicoRepository.findAll();
    }

    /**
     * Busca un registro histórico por su ID. Lanza ResourceNotFoundException si no existe.
     */
    @Transactional(readOnly = true)
    public Historico obtenerPorId(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Histórico no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Registro histórico no encontrado con el ID: " + id);
                });
    }

    /**
     * Elimina un registro histórico por su ID.
     */
    @Transactional
    public void eliminar(Long id) {
        logger.info("Eliminando histórico con ID: {}", id);
        Historico historico = obtenerPorId(id); // Valida existencia antes de borrar
        historicoRepository.delete(historico);
    }

    /**
     * Filtra los históricos por método de pago (V2).
     */
    @Transactional(readOnly = true)
    public List<Historico> listarPorMetodoPago(String metodoPago) {
        return historicoRepository.findByMetodoPago(metodoPago);
    }

    /**
     * Filtra los históricos por un rango de fechas específico (V2).
     */
    @Transactional(readOnly = true)
    public List<Historico> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        logger.info("Buscando históricos entre {} y {}", inicio, fin);
        return historicoRepository.findByFechaVentaBetween(inicio, fin);
    }
}