package com.vet.animalink.historico_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.vet.animalink.historico_service.model.Historico;
import com.vet.animalink.historico_service.repository.HistoricoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HistoricoServiceTest {
    private HistoricoService historicoService;
    @Mock
    private HistoricoRepository historicoRepository;

    @BeforeEach
    void setUp() {
        historicoService = new HistoricoService(historicoRepository);
    }

    @Test
    void testGuardar() {
        Historico historico = new Historico(1L, 100, 5, 19, 114, "EFECTIVO", new Date());
        when(historicoRepository.save(any(Historico.class))).thenReturn(historico);
        Historico resultado = historicoService.guardar(historico);
        assertNotNull(resultado);
        assertEquals(100, resultado.getTotalNeto());
        assertEquals("EFECTIVO", resultado.getMetodoPago());
        verify(historicoRepository).save(any(Historico.class));
    }

    @Test
    void testObtenerPorId() {
        Long id = 1L;
        Historico historico = new Historico(id, 300, 0, 57, 357, "TRANSFER", new Date());
        when(historicoRepository.findById(id)).thenReturn(Optional.of(historico));
        Historico resultado = historicoService.obtenerPorId(id);
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(historicoRepository).findById(id);
    }
}
