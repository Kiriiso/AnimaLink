package com.vet.animalink.cliente_service.repository;

import com.vet.animalink.cliente_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByRut(String rut);

    Optional<Cliente> findByEmail(String email);

    boolean existsByRut(String rut);

    boolean existsByEmail(String email);

    // Operaciones personalizadas (más allá del CRUD básico)
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);

    List<Cliente> findByActivoTrue();
}
