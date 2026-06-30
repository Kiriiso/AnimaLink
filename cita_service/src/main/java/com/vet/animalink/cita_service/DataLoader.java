package com.vet.animalink.cita_service;

import com.vet.animalink.cita_service.model.Cita;
import com.vet.animalink.cita_service.model.enums.EstadoCita;
import com.vet.animalink.cita_service.repository.CitaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga citas de prueba con DataFaker.
 * mascotaId (1..20) y veterinarioId (1..10) coinciden con los datos de los otros servicios.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final CitaRepository citaRepository;

    public DataLoader(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public void run(String... args) {
        if (citaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] motivos = {"Vacunación", "Control general", "Desparasitación", "Cirugía menor",
                "Urgencia", "Control post-operatorio", "Consulta dermatológica"};
        EstadoCita[] estados = EstadoCita.values();

        IntStream.rangeClosed(1, 18).forEach(i -> {
            Cita cita = Cita.builder()
                    .mascotaId((long) faker.number().numberBetween(1, 20))
                    .veterinarioId((long) faker.number().numberBetween(1, 10))
                    .fechaHora(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 30))
                            .withHour(faker.number().numberBetween(9, 18)).withMinute(0))
                    .motivo(motivos[faker.number().numberBetween(0, motivos.length)])
                    .estado(estados[faker.number().numberBetween(0, estados.length)])
                    .observaciones(faker.lorem().sentence(6))
                    .build();
            citaRepository.save(cita);
        });
    }
}
