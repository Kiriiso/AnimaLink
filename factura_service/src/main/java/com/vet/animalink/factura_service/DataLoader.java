package com.vet.animalink.factura_service;

import com.vet.animalink.factura_service.model.DetalleFactura;
import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.model.enums.EstadoFactura;
import com.vet.animalink.factura_service.repository.FacturaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga facturas de prueba con DataFaker (con su detalle).
 * Genera los datos localmente (no llama a otros servicios) para no acoplar el arranque.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final FacturaRepository facturaRepository;

    public DataLoader(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public void run(String... args) {
        if (facturaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));
        String[] insumos = {"Amoxicilina 500mg", "Vacuna Antirrábica", "Pipeta antipulgas",
                "Alimento medicado 5kg", "Shampoo dermatológico"};
        EstadoFactura[] estados = EstadoFactura.values();

        IntStream.rangeClosed(1, 8).forEach(i -> {
            Factura factura = Factura.builder()
                    .clienteId((long) faker.number().numberBetween(1, 15))
                    .fecha(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 90)))
                    .estado(estados[faker.number().numberBetween(0, estados.length)])
                    .total(BigDecimal.ZERO)
                    .build();

            BigDecimal total = BigDecimal.ZERO;
            int lineas = faker.number().numberBetween(1, 4);
            for (int j = 0; j < lineas; j++) {
                int cantidad = faker.number().numberBetween(1, 5);
                BigDecimal precio = BigDecimal.valueOf(faker.number().numberBetween(1000, 15000));
                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));

                DetalleFactura detalle = DetalleFactura.builder()
                        .insumoId((long) faker.number().numberBetween(1, 10))
                        .descripcion(insumos[faker.number().numberBetween(0, insumos.length)])
                        .cantidad(cantidad)
                        .precioUnitario(precio)
                        .subtotal(subtotal)
                        .build();
                factura.agregarDetalle(detalle);
                total = total.add(subtotal);
            }
            factura.setTotal(total);
            facturaRepository.save(factura);
        });
    }
}
