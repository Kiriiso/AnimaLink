package com.vet.animalink.cliente_service;

import com.vet.animalink.cliente_service.model.Cliente;
import com.vet.animalink.cliente_service.repository.ClienteRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.stream.IntStream;

/**
 * Carga datos de prueba automáticos al iniciar el microservicio usando DataFaker.
 * Solo inserta si la tabla está vacía (no duplica en cada arranque).
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    public DataLoader(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        if (clienteRepository.count() > 0) {
            return; // ya hay datos, no recargar
        }

        Faker faker = new Faker(Locale.forLanguageTag("es"));

        IntStream.rangeClosed(1, 15).forEach(i -> {
            String nombre = faker.name().firstName();
            String apellido = faker.name().lastName();
            int numeroRut = faker.number().numberBetween(5_000_000, 25_000_000);
            String dv = String.valueOf(faker.number().numberBetween(0, 9));

            Cliente cliente = Cliente.builder()
                    .rut(numeroRut + "-" + dv)
                    .nombre(nombre)
                    .apellido(apellido)
                    .email((nombre + "." + apellido + i + "@correo.cl")
                            .toLowerCase().replace(" ", ""))
                    .telefono("+569" + faker.number().numberBetween(10_000_000, 99_999_999))
                    .direccion(faker.address().fullAddress())
                    .activo(faker.bool().bool() || i % 3 != 0) // mayoría activos
                    .build();

            clienteRepository.save(cliente);
        });
    }
}
