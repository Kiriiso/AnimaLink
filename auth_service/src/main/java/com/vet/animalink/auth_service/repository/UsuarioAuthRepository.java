package com.vet.animalink.auth_service.repository;

import com.vet.animalink.auth_service.model.UsuarioAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioAuthRepository extends JpaRepository<UsuarioAuth, Long> {
    Optional<UsuarioAuth> findByUsername(String username);
    boolean existsByUsername(String username);
}