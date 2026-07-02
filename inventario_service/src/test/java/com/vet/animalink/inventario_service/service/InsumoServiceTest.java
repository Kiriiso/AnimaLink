package com.vet.animalink.inventario_service.service;

import com.vet.animalink.inventario_service.exception.ResourceNotFoundException;
import com.vet.animalink.inventario_service.model.Insumo;
import com.vet.animalink.inventario_service.model.enums.Categoria;
import com.vet.animalink.inventario_service.repository.InsumoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsumoServiceTest {

    @Mock private InsumoRepository insumoRepository;
    @InjectMocks private InsumoService insumoService;

    private Insumo insumo;

    @BeforeEach
    void setUp() {
        insumo = Insumo.builder()
                .id(1L)
                .nombre("Amoxicilina 500mg")
                .categoria(Categoria.MEDICAMENTO)
                .stock(10)
                .stockMinimo(5)
                .precioCosto(new BigDecimal("1500"))
                .precioVenta(new BigDecimal("2500"))
                .build();
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(insumoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> insumoService.obtenerPorId(99L));
    }

    @Test
    void ajustarStock_descuentoValido_actualiza() {
        when(insumoRepository.findById(1L)).thenReturn(Optional.of(insumo));
        when(insumoRepository.save(any(Insumo.class))).thenAnswer(inv -> inv.getArgument(0));

        Insumo ajustado = insumoService.ajustarStock(1L, -3);
        assertEquals(7, ajustado.getStock());
    }

    @Test
    void ajustarStock_descuentoMayorQueStock_lanzaError() {
        when(insumoRepository.findById(1L)).thenReturn(Optional.of(insumo));
        assertThrows(IllegalArgumentException.class, () -> insumoService.ajustarStock(1L, -50));
        verify(insumoRepository, never()).save(any());
    }

    @Test
    void crear_guarda() {
        when(insumoRepository.save(insumo)).thenReturn(insumo);
        Insumo creado = insumoService.crear(insumo);
        assertEquals("Amoxicilina 500mg", creado.getNombre());
    }
}
