package com.vet.animalink.historial_service.service;

import com.vet.animalink.historial_service.client.MascotaClient;
import com.vet.animalink.historial_service.client.dto.MascotaDTO;
import com.vet.animalink.historial_service.exception.ResourceNotFoundException;
import com.vet.animalink.historial_service.model.Historial;
import com.vet.animalink.historial_service.repository.HistorialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialService {

    private final HistorialRepository historialRepository;
    private final MascotaClient mascotaClient;

    public HistorialService(HistorialRepository historialRepository, MascotaClient mascotaClient) {
        this.historialRepository = historialRepository;
        this.mascotaClient = mascotaClient;
    }

    public List<Historial> listar() {
        return historialRepository.findAll();
    }

    public Historial obtenerPorId(Long id) {
        return historialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado con id " + id));
    }

    @Transactional
    public Historial crear(Historial historial) {
        // Valida que la mascota exista en mascota_service
        mascotaClient.obtenerMascota(historial.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede registrar el historial: la mascota con id "
                                + historial.getMascotaId() + " no existe"));
        return historialRepository.save(historial);
    }

    @Transactional
    public Historial actualizar(Long id, Historial datos) {
        Historial existente = obtenerPorId(id);
        existente.setFecha(datos.getFecha());
        existente.setDiagnostico(datos.getDiagnostico());
        existente.setTratamiento(datos.getTratamiento());
        existente.setObservaciones(datos.getObservaciones());
        return historialRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Historial existente = obtenerPorId(id);
        historialRepository.delete(existente);
    }

    public Optional<MascotaDTO> obtenerMascota(Long mascotaId) {
        return mascotaClient.obtenerMascota(mascotaId);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Historial> listarPorMascota(Long mascotaId) {
        return historialRepository.findByMascotaIdOrderByFechaDesc(mascotaId);
    }

    public List<Historial> buscarPorDiagnostico(String texto) {
        return historialRepository.findByDiagnosticoContainingIgnoreCase(texto);
    }
}
