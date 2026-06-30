package com.vet.animalink.mascota_service.service;

import com.vet.animalink.mascota_service.client.ClienteClient;
import com.vet.animalink.mascota_service.client.dto.ClienteDTO;
import com.vet.animalink.mascota_service.exception.ResourceNotFoundException;
import com.vet.animalink.mascota_service.model.Mascota;
import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.repository.MascotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final ClienteClient clienteClient;

    public MascotaService(MascotaRepository mascotaRepository, ClienteClient clienteClient) {
        this.mascotaRepository = mascotaRepository;
        this.clienteClient = clienteClient;
    }

    public List<Mascota> listar() {
        return mascotaRepository.findAll();
    }

    public Mascota obtenerPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada con id " + id));
    }

    @Transactional
    public Mascota crear(Mascota mascota) {
        // COMUNICACIÓN ENTRE MICROSERVICIOS: valida que el dueño exista en cliente_service
        clienteClient.obtenerCliente(mascota.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se puede crear la mascota: el cliente con id "
                                + mascota.getClienteId() + " no existe"));
        return mascotaRepository.save(mascota);
    }

    @Transactional
    public Mascota actualizar(Long id, Mascota datos) {
        Mascota existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setEspecie(datos.getEspecie());
        existente.setRaza(datos.getRaza());
        existente.setSexo(datos.getSexo());
        existente.setFechaNacimiento(datos.getFechaNacimiento());
        existente.setColor(datos.getColor());
        return mascotaRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Mascota existente = obtenerPorId(id);
        mascotaRepository.delete(existente);
    }

    /** Consulta el dueño en cliente_service (para enriquecer la respuesta). */
    public Optional<ClienteDTO> obtenerDueno(Long clienteId) {
        return clienteClient.obtenerCliente(clienteId);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Mascota> listarPorCliente(Long clienteId) {
        return mascotaRepository.findByClienteId(clienteId);
    }

    public List<Mascota> listarPorEspecie(Especie especie) {
        return mascotaRepository.findByEspecie(especie);
    }
}
