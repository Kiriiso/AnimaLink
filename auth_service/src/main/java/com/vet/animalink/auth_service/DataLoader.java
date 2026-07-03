package com.vet.animalink.auth_service;

import com.vet.animalink.auth_service.model.Rol;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioAuthRepository repository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioAuthRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        // Asignamos IDs fijos ficticios (1L, 2L, 3L) para los usuarios iniciales
        crear(1L, "admin", "admin123", Rol.ADMIN);
        crear(2L, "vet", "vet123", Rol.VETERINARIO);
        crear(3L, "recep", "recep123", Rol.RECEPCIONISTA);

        // Usuarios adicionales aleatorios
        Faker faker = new Faker(Locale.forLanguageTag("es"));
        Rol[] roles = Rol.values();
        for (int i = 1; i <= 5; i++) {
            String username = faker.internet().username() + i;
            // Para evitar duplicados en la restricción UNIQUE, sumamos al contador
            Long mockUsuarioId = 3L + i; 
            
            crear(mockUsuarioId, username, "password" + i, roles[faker.number().numberBetween(0, roles.length)]);
        }
    }

    // 1. Añadimos el parámetro Long usuarioId al método
    private void crear(Long usuarioId, String username, String passwordPlano, Rol rol) {
        UsuarioAuth usuario = UsuarioAuth.builder()
                .username(username)
                .password(passwordEncoder.encode(passwordPlano))
                .rol(rol)
                .usuarioId(usuarioId) // 
                .activo(true)
                .build();
        repository.save(usuario);
    }
}