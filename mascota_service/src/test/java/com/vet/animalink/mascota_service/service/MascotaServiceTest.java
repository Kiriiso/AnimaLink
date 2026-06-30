package com.vet.animalink.mascota_service.service;

import com.vet.animalink.mascota_service.client.ClienteClient;
import com.vet.animalink.mascota_service.client.dto.ClienteDTO;
import com.vet.animalink.mascota_service.exception.ResourceNotFoundException;
import com.vet.animalink.mascota_service.model.Mascota;
import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.model.enums.Sexo;
import com.vet.animalink.mascota_service.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota mascota;

    @BeforeEach
    void setUp() {
        mascota = Mascota.builder()
                .id(1L)
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .raza("Labrador")
                .sexo(Sexo.MACHO)
                .color("Café")
                .clienteId(3L)
                .activo(true)
                .build();
    }

    @Test
    void crear_conDuenoExistente_guarda() {
        when(clienteClient.obtenerCliente(3L))
                .thenReturn(Optional.of(new ClienteDTO(3L, "María", "González")));
        when(mascotaRepository.save(mascota)).thenReturn(mascota);

        Mascota creada = mascotaService.crear(mascota);

        assertNotNull(creada);
        verify(mascotaRepository, times(1)).save(mascota);
    }

    @Test
    void crear_conDuenoInexistente_lanza404() {
        // cliente_service responde que el dueño no existe
        when(clienteClient.obtenerCliente(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> mascotaService.crear(mascota));
        verify(mascotaRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> mascotaService.obtenerPorId(99L));
    }

    @Test
    void obtenerPorId_existente_devuelveMascota() {
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        Mascota encontrada = mascotaService.obtenerPorId(1L);
        assertEquals("Firulais", encontrada.getNombre());
    }
}
