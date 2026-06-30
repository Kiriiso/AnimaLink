package com.vet.animalink.historial_service.service;

import com.vet.animalink.historial_service.client.MascotaClient;
import com.vet.animalink.historial_service.client.dto.MascotaDTO;
import com.vet.animalink.historial_service.exception.ResourceNotFoundException;
import com.vet.animalink.historial_service.model.Historial;
import com.vet.animalink.historial_service.repository.HistorialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialServiceTest {

    @Mock private HistorialRepository historialRepository;
    @Mock private MascotaClient mascotaClient;

    @InjectMocks private HistorialService historialService;

    private Historial historial;

    @BeforeEach
    void setUp() {
        historial = Historial.builder()
                .id(1L)
                .mascotaId(5L)
                .fecha(LocalDate.now().minusDays(3))
                .diagnostico("Otitis externa")
                .tratamiento("Antibiótico tópico")
                .build();
    }

    @Test
    void crear_conMascotaExistente_guarda() {
        when(mascotaClient.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));
        when(historialRepository.save(historial)).thenReturn(historial);

        Historial creado = historialService.crear(historial);
        assertEquals("Otitis externa", creado.getDiagnostico());
        verify(historialRepository).save(historial);
    }

    @Test
    void crear_conMascotaInexistente_lanza404() {
        when(mascotaClient.obtenerMascota(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> historialService.crear(historial));
        verify(historialRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> historialService.obtenerPorId(99L));
    }
}
