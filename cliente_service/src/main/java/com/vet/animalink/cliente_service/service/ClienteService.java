package com.vet.animalink.cliente_service.service;

import com.vet.animalink.cliente_service.exception.ResourceNotFoundException;
import com.vet.animalink.cliente_service.model.Cliente;
import com.vet.animalink.cliente_service.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Lógica de negocio del microservicio de clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
    }

    @Transactional
    public Cliente crear(Cliente cliente) {
        if (clienteRepository.existsByRut(cliente.getRut())) {
            throw new IllegalArgumentException("Ya existe un cliente con el RUT " + cliente.getRut());
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email " + cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente actualizar(Long id, Cliente datos) {
        Cliente existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setDireccion(datos.getDireccion());
        return clienteRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente existente = obtenerPorId(id);
        clienteRepository.delete(existente);
    }

    // ---- Operaciones personalizadas (V2) ----

    public List<Cliente> buscarPorNombre(String texto) {
        return clienteRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(texto, texto);
    }

    public List<Cliente> listarActivos() {
        return clienteRepository.findByActivoTrue();
    }
}
