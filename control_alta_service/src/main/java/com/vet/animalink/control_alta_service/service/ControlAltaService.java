package com.vet.animalink.control_alta_service.service;

import com.vet.animalink.control_alta_service.client.MascotaClient;
import com.vet.animalink.control_alta_service.client.dto.MascotaDTO;
import com.vet.animalink.control_alta_service.exception.ResourceNotFoundException;
import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import com.vet.animalink.control_alta_service.repository.ControlAltaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ControlAltaService {

    private final ControlAltaRepository controlAltaRepository;
    private final MascotaClient mascotaClient;

    public ControlAltaService(ControlAltaRepository controlAltaRepository, MascotaClient mascotaClient) {
        this.controlAltaRepository = controlAltaRepository;
        this.mascotaClient = mascotaClient;
    }

    public List<ControlAlta> listar() {
        return controlAltaRepository.findAll();
    }

    public ControlAlta obtenerPorId(Long id) {
        return controlAltaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Control no encontrado con id " + id));
    }

    @Transactional
    public ControlAlta registrarIngreso(ControlAlta control) {
        // Valida que la mascota exista en mascota_service
        mascotaClient.obtenerMascota(control.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede registrar el ingreso: la mascota con id "
                                + control.getMascotaId() + " no existe"));
        control.setFechaIngreso(LocalDateTime.now());
        control.setEstado(EstadoControl.HOSPITALIZADO);
        control.setFechaAlta(null);
        return controlAltaRepository.save(control);
    }

    @Transactional
    public ControlAlta darDeAlta(Long id, String observaciones) {
        ControlAlta control = obtenerPorId(id);
        if (control.getEstado() == EstadoControl.DADO_DE_ALTA) {
            throw new IllegalArgumentException("El control " + id + " ya fue dado de alta");
        }
        control.setEstado(EstadoControl.DADO_DE_ALTA);
        control.setFechaAlta(LocalDateTime.now());
        if (observaciones != null && !observaciones.isBlank()) {
            control.setObservaciones(observaciones);
        }
        return controlAltaRepository.save(control);
    }

    @Transactional
    public void eliminar(Long id) {
        ControlAlta control = obtenerPorId(id);
        controlAltaRepository.delete(control);
    }

    public Optional<MascotaDTO> obtenerMascota(Long mascotaId) {
        return mascotaClient.obtenerMascota(mascotaId);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<ControlAlta> listarPorEstado(EstadoControl estado) {
        return controlAltaRepository.findByEstado(estado);
    }

    public List<ControlAlta> listarPorMascota(Long mascotaId) {
        return controlAltaRepository.findByMascotaId(mascotaId);
    }
}
