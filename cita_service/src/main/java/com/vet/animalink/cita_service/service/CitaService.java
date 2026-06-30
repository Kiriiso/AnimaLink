package com.vet.animalink.cita_service.service;

import com.vet.animalink.cita_service.client.MascotaClient;
import com.vet.animalink.cita_service.client.UsuarioClient;
import com.vet.animalink.cita_service.client.dto.MascotaDTO;
import com.vet.animalink.cita_service.client.dto.UsuarioDTO;
import com.vet.animalink.cita_service.exception.ResourceNotFoundException;
import com.vet.animalink.cita_service.model.Cita;
import com.vet.animalink.cita_service.model.enums.EstadoCita;
import com.vet.animalink.cita_service.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaClient mascotaClient;
    private final UsuarioClient usuarioClient;

    public CitaService(CitaRepository citaRepository,
                       MascotaClient mascotaClient,
                       UsuarioClient usuarioClient) {
        this.citaRepository = citaRepository;
        this.mascotaClient = mascotaClient;
        this.usuarioClient = usuarioClient;
    }

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id " + id));
    }

    @Transactional
    public Cita crear(Cita cita) {
        // 1) La mascota debe existir (mascota_service)
        mascotaClient.obtenerMascota(cita.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede agendar: la mascota con id " + cita.getMascotaId() + " no existe"));

        // 2) El veterinario debe existir (usuario_service) y tener rol VETERINARIO
        UsuarioDTO vet = usuarioClient.obtenerUsuario(cita.getVeterinarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede agendar: el veterinario con id " + cita.getVeterinarioId() + " no existe"));
        if (vet.rol() != null && !"VETERINARIO".equalsIgnoreCase(vet.rol())) {
            throw new IllegalArgumentException(
                    "El usuario " + cita.getVeterinarioId() + " no es un VETERINARIO (rol: " + vet.rol() + ")");
        }

        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCita.PROGRAMADA);
        }
        return citaRepository.save(cita);
    }

    @Transactional
    public Cita actualizar(Long id, Cita datos) {
        Cita existente = obtenerPorId(id);
        existente.setFechaHora(datos.getFechaHora());
        existente.setMotivo(datos.getMotivo());
        existente.setObservaciones(datos.getObservaciones());
        return citaRepository.save(existente);
    }

    @Transactional
    public Cita cambiarEstado(Long id, EstadoCita estado) {
        Cita existente = obtenerPorId(id);
        existente.setEstado(estado);
        return citaRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Cita existente = obtenerPorId(id);
        citaRepository.delete(existente);
    }

    public Optional<MascotaDTO> obtenerMascota(Long mascotaId) {
        return mascotaClient.obtenerMascota(mascotaId);
    }

    public Optional<UsuarioDTO> obtenerVeterinario(Long veterinarioId) {
        return usuarioClient.obtenerUsuario(veterinarioId);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Cita> listarPorMascota(Long mascotaId) {
        return citaRepository.findByMascotaId(mascotaId);
    }

    public List<Cita> listarPorVeterinario(Long veterinarioId) {
        return citaRepository.findByVeterinarioId(veterinarioId);
    }

    public List<Cita> listarPorEstado(EstadoCita estado) {
        return citaRepository.findByEstado(estado);
    }
}
