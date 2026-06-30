package com.vet.animalink.mascota_service;

import com.vet.animalink.mascota_service.model.Mascota;
import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.model.enums.Sexo;
import com.vet.animalink.mascota_service.repository.MascotaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga mascotas de prueba con DataFaker.
 * El clienteId se asigna aleatorio entre 1 y 15 (coincide con los clientes que
 * genera cliente_service). No valida contra cliente_service para no acoplar el arranque.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final MascotaRepository mascotaRepository;

    public DataLoader(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public void run(String... args) {
        if (mascotaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] razas = {"Labrador", "Poodle", "Bulldog", "Siamés", "Persa", "Mestizo", "Pastor Alemán", "Beagle"};

        IntStream.rangeClosed(1, 20).forEach(i -> {
            Mascota mascota = Mascota.builder()
                    .nombre(faker.name().firstName())
                    .especie(faker.options().option(Especie.class))
                    .raza(faker.options().option(razas))
                    .sexo(faker.options().option(Sexo.class))
                    .fechaNacimiento(LocalDate.now().minusDays(faker.number().numberBetween(60, 4000)))
                    .color(faker.color().name())
                    .clienteId((long) faker.number().numberBetween(1, 15))
                    .activo(true)
                    .build();

            mascotaRepository.save(mascota);
        });
    }
}
