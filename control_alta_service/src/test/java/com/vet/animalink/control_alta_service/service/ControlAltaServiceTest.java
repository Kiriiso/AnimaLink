package com.vet.animalink.control_alta_service.service;

import com.vet.animalink.control_alta_service.client.MascotaClient;
import com.vet.animalink.control_alta_service.client.dto.MascotaDTO;
import com.vet.animalink.control_alta_service.exception.ResourceNotFoundException;
import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import com.vet.animalink.control_alta_service.repository.ControlAltaRepository;
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
class ControlAltaServiceTest {

    @Mock private ControlAltaRepository controlAltaRepository;
    @Mock private MascotaClient mascotaClient;

    @InjectMocks private ControlAltaService controlAltaService;

    private ControlAlta control;

    @BeforeEach
    void setUp() {
        control = ControlAlta.builder()
                .id(1L)
                .mascotaId(5L)
                .motivoIngreso("Post-operatorio")
                .estado(EstadoControl.HOSPITALIZADO)
                .build();
    }

    @Test
    void registrarIngreso_conMascotaExistente_guarda() {
        when(mascotaClient.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));
        when(controlAltaRepository.save(any(ControlAlta.class))).thenAnswer(inv -> inv.getArgument(0));

        ControlAlta creado = controlAltaService.registrarIngreso(control);
        assertEquals(EstadoControl.HOSPITALIZADO, creado.getEstado());
        assertNotNull(creado.getFechaIngreso());
    }

    @Test
    void registrarIngreso_conMascotaInexistente_lanza404() {
        when(mascotaClient.obtenerMascota(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> controlAltaService.registrarIngreso(control));
        verify(controlAltaRepository, never()).save(any());
    }

    @Test
    void darDeAlta_hospitalizado_cambiaEstadoYFecha() {
        when(controlAltaRepository.findById(1L)).thenReturn(Optional.of(control));
        when(controlAltaRepository.save(any(ControlAlta.class))).thenAnswer(inv -> inv.getArgument(0));

        ControlAlta dado = controlAltaService.darDeAlta(1L, "Recuperado");
        assertEquals(EstadoControl.DADO_DE_ALTA, dado.getEstado());
        assertNotNull(dado.getFechaAlta());
    }

    @Test
    void darDeAlta_yaDadoDeAlta_lanzaError() {
        control.setEstado(EstadoControl.DADO_DE_ALTA);
        when(controlAltaRepository.findById(1L)).thenReturn(Optional.of(control));
        assertThrows(IllegalArgumentException.class, () -> controlAltaService.darDeAlta(1L, null));
    }
}
