package com.vet.animalink.control_alta_service;

import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import com.vet.animalink.control_alta_service.repository.ControlAltaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga controles de hospitalización de prueba con DataFaker.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final ControlAltaRepository controlAltaRepository;

    public DataLoader(ControlAltaRepository controlAltaRepository) {
        this.controlAltaRepository = controlAltaRepository;
    }

    @Override
    public void run(String... args) {
        if (controlAltaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] motivos = {"Post-operatorio", "Deshidratación severa", "Observación por intoxicación",
                "Recuperación de cirugía", "Cuadro infeccioso"};

        IntStream.rangeClosed(1, 12).forEach(i -> {
            boolean dadoDeAlta = faker.bool().bool();
            LocalDateTime ingreso = LocalDateTime.now().minusDays(faker.number().numberBetween(1, 60));

            ControlAlta control = ControlAlta.builder()
                    .mascotaId((long) faker.number().numberBetween(1, 20))
                    .fechaIngreso(ingreso)
                    .fechaAlta(dadoDeAlta ? ingreso.plusDays(faker.number().numberBetween(1, 7)) : null)
                    .motivoIngreso(motivos[faker.number().numberBetween(0, motivos.length)])
                    .estado(dadoDeAlta ? EstadoControl.DADO_DE_ALTA : EstadoControl.HOSPITALIZADO)
                    .observaciones(faker.lorem().sentence(6))
                    .build();
            controlAltaRepository.save(control);
        });
    }
}
