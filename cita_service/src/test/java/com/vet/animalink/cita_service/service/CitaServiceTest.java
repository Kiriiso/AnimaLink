package com.vet.animalink.cita_service.service;

import com.vet.animalink.cita_service.client.MascotaClient;
import com.vet.animalink.cita_service.client.UsuarioClient;
import com.vet.animalink.cita_service.client.dto.MascotaDTO;
import com.vet.animalink.cita_service.client.dto.UsuarioDTO;
import com.vet.animalink.cita_service.exception.ResourceNotFoundException;
import com.vet.animalink.cita_service.model.Cita;
import com.vet.animalink.cita_service.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock private CitaRepository citaRepository;
    @Mock private MascotaClient mascotaClient;
    @Mock private UsuarioClient usuarioClient;

    @InjectMocks private CitaService citaService;

    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = Cita.builder()
                .id(1L)
                .mascotaId(5L)
                .veterinarioId(2L)
                .fechaHora(LocalDateTime.now().plusDays(2))
                .motivo("Vacunación")
                .build();
    }

    @Test
    void crear_conMascotaYVeterinarioValidos_guarda() {
        when(mascotaClient.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));
        when(usuarioClient.obtenerUsuario(2L))
                .thenReturn(Optional.of(new UsuarioDTO(2L, "Carlos", "Pérez", "VETERINARIO")));
        when(citaRepository.save(cita)).thenReturn(cita);

        Cita creada = citaService.crear(cita);
        assertNotNull(creada);
        verify(citaRepository).save(cita);
    }

    @Test
    void crear_conMascotaInexistente_lanza404() {
        when(mascotaClient.obtenerMascota(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> citaService.crear(cita));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void crear_conUsuarioQueNoEsVeterinario_lanzaError() {
        when(mascotaClient.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));
        when(usuarioClient.obtenerUsuario(2L))
                .thenReturn(Optional.of(new UsuarioDTO(2L, "Ana", "Soto", "RECEPCIONISTA")));

        assertThrows(IllegalArgumentException.class, () -> citaService.crear(cita));
        verify(citaRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> citaService.obtenerPorId(99L));
    }
}
