package com.vet.animalink.auth_service;

import com.vet.animalink.auth_service.model.Rol;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Crea usuarios de prueba con contraseñas ENCRIPTADAS (BCrypt) usando DataFaker.
 * Incluye 3 cuentas fijas para poder iniciar sesión de inmediato:
 *   admin / admin123   (ADMIN)
 *   vet   / vet123     (VETERINARIO)
 *   recep / recep123   (RECEPCIONISTA)
 */
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

        crear("admin", "admin123", Rol.ADMIN);
        crear("vet", "vet123", Rol.VETERINARIO);
        crear("recep", "recep123", Rol.RECEPCIONISTA);

        // Usuarios adicionales aleatorios
        Faker faker = new Faker(Locale.forLanguageTag("es"));
        Rol[] roles = Rol.values();
        for (int i = 1; i <= 5; i++) {
            String username = faker.internet().username() + i;
            crear(username, "password" + i, roles[faker.number().numberBetween(0, roles.length)]);
        }
    }

    private void crear(String username, String passwordPlano, Rol rol) {
        UsuarioAuth usuario = UsuarioAuth.builder()
                .username(username)
                .password(passwordEncoder.encode(passwordPlano)) // se guarda ENCRIPTADA
                .rol(rol)
                .activo(true)
                .build();
        repository.save(usuario);
    }
}
