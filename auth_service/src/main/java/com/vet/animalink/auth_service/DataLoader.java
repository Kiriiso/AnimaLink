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

        // 1. Cuenta de servicio obligatoria para la comunicación entre microservicios
        crear("sistema-interno", "ClaveSegura123!", Rol.ADMIN, 0L);

        // 2. Usuarios base de prueba
        crear("admin", "admin123", Rol.ADMIN, 1L);
        crear("vet", "vet123", Rol.VETERINARIO, 2L);
        crear("recep", "recep123", Rol.RECEPCIONISTA, 3L);

        // 3. Usuarios aleatorios adicionales
        Faker faker = new Faker(Locale.forLanguageTag("es"));
        Rol[] roles = Rol.values();
        long usuarioIdCounter = 4L;

        for (int i = 1; i <= 5; i++) {
            String username = faker.internet().username() + i;
            Rol rolAleatorio = roles[faker.number().numberBetween(0, roles.length)];
            
            crear(username, "password" + i, rolAleatorio, usuarioIdCounter++);
        }
    }

    private void crear(String username, String passwordPlano, Rol rol, Long usuarioId) {
        UsuarioAuth usuario = UsuarioAuth.builder()
                .username(username)
                .password(passwordEncoder.encode(passwordPlano))
                .rol(rol)
                .usuarioId(usuarioId)
                .activo(true)
                .build();

        repository.save(usuario);
    }
}