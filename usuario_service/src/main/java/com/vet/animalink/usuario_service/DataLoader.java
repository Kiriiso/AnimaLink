package com.vet.animalink.usuario_service;

import com.vet.animalink.usuario_service.model.Usuario;
import com.vet.animalink.usuario_service.model.enums.Rol;
import com.vet.animalink.usuario_service.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga personal de prueba con DataFaker (admins, veterinarios y recepcionistas).
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataLoader(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] especialidades = {"Cirugía", "Medicina general", "Dermatología", "Odontología", "Traumatología"};
        Rol[] roles = Rol.values();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            String nombre = faker.name().firstName();
            String apellido = faker.name().lastName();
            // el primero es ADMIN, el resto rol aleatorio
            Rol rol = (i == 1) ? Rol.ADMIN : roles[faker.number().numberBetween(0, roles.length)];
            int numeroRut = faker.number().numberBetween(5_000_000, 25_000_000);

            Usuario usuario = Usuario.builder()
                    .rut(numeroRut + "-" + faker.number().numberBetween(0, 9))
                    .nombre(nombre)
                    .apellido(apellido)
                    .email((nombre + "." + apellido + i + "@animalink.cl").toLowerCase().replace(" ", ""))
                    .telefono("+569" + faker.number().numberBetween(10_000_000, 99_999_999))
                    .rol(rol)
                    .especialidad(rol == Rol.VETERINARIO ? especialidades[faker.number().numberBetween(0, especialidades.length)] : null)
                    .activo(true)
                    .build();

            usuarioRepository.save(usuario);
        });
    }
}
