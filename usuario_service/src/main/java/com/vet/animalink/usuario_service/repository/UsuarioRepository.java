package com.vet.animalink.usuario_service.repository;

import com.vet.animalink.usuario_service.model.Usuario;
import com.vet.animalink.usuario_service.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByRut(String rut);

    Optional<Usuario> findByEmail(String email);

    boolean existsByRut(String rut);

    boolean existsByEmail(String email);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByActivoTrue();

    List<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
