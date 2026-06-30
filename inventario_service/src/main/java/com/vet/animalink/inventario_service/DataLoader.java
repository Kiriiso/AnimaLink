package com.vet.animalink.inventario_service;

import com.vet.animalink.inventario_service.model.Insumo;
import com.vet.animalink.inventario_service.model.enums.Categoria;
import com.vet.animalink.inventario_service.repository.InsumoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga insumos de prueba con DataFaker.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final InsumoRepository insumoRepository;

    public DataLoader(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    @Override
    public void run(String... args) {
        if (insumoRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] nombres = {"Amoxicilina 500mg", "Vacuna Antirrábica", "Suero fisiológico", "Pipeta antipulgas",
                "Alimento medicado 5kg", "Jeringa 5ml", "Gasa estéril", "Ivermectina", "Shampoo dermatológico",
                "Collar isabelino"};
        Categoria[] categorias = Categoria.values();

        IntStream.range(0, nombres.length).forEach(i -> {
            int costo = faker.number().numberBetween(500, 20000);
            Insumo insumo = Insumo.builder()
                    .nombre(nombres[i])
                    .categoria(categorias[faker.number().numberBetween(0, categorias.length)])
                    .descripcion(faker.lorem().sentence(5))
                    .stock(faker.number().numberBetween(0, 150))
                    .stockMinimo(faker.number().numberBetween(5, 20))
                    .precioCosto(BigDecimal.valueOf(costo))
                    .precioVenta(BigDecimal.valueOf(Math.round(costo * 1.6)))
                    .fechaVencimiento(LocalDate.now().plusDays(faker.number().numberBetween(30, 900)))
                    .activo(true)
                    .build();
            insumoRepository.save(insumo);
        });
    }
}
