package com.vet.animalink.historial_service;

import com.vet.animalink.historial_service.model.Historial;
import com.vet.animalink.historial_service.repository.HistorialRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga historiales clínicos de prueba con DataFaker.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final HistorialRepository historialRepository;

    public DataLoader(HistorialRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @Override
    public void run(String... args) {
        if (historialRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] diagnosticos = {"Otitis externa", "Gastroenteritis", "Fractura de pata", "Dermatitis alérgica",
                "Control sano", "Parásitos intestinales", "Conjuntivitis"};
        String[] tratamientos = {"Antibiótico oral", "Dieta blanda y suero", "Inmovilización y reposo",
                "Antihistamínico", "Desparasitante", "Colirio antibiótico"};

        IntStream.rangeClosed(1, 20).forEach(i -> {
            Historial h = Historial.builder()
                    .mascotaId((long) faker.number().numberBetween(1, 20))
                    .fecha(LocalDate.now().minusDays(faker.number().numberBetween(1, 365)))
                    .diagnostico(diagnosticos[faker.number().numberBetween(0, diagnosticos.length)])
                    .tratamiento(tratamientos[faker.number().numberBetween(0, tratamientos.length)])
                    .observaciones(faker.lorem().sentence(8))
                    .build();
            historialRepository.save(h);
        });
    }
}
