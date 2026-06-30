package com.vet.animalink.factura_service.service;

import com.vet.animalink.factura_service.client.ClienteClient;
import com.vet.animalink.factura_service.client.InsumoClient;
import com.vet.animalink.factura_service.client.dto.ClienteDTO;
import com.vet.animalink.factura_service.client.dto.InsumoDTO;
import com.vet.animalink.factura_service.dto.FacturaRequest;
import com.vet.animalink.factura_service.exception.ResourceNotFoundException;
import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.repository.FacturaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServiceTest {

    @Mock private FacturaRepository facturaRepository;
    @Mock private ClienteClient clienteClient;
    @Mock private InsumoClient insumoClient;

    @InjectMocks private FacturaService facturaService;

    @Test
    void emitir_valido_calculaTotalYDescuentaStock() {
        FacturaRequest request = new FacturaRequest(3L,
                List.of(new FacturaRequest.ItemRequest(4L, 2)));

        when(clienteClient.obtenerCliente(3L))
                .thenReturn(Optional.of(new ClienteDTO(3L, "María", "González")));
        when(insumoClient.obtenerInsumo(4L))
                .thenReturn(Optional.of(new InsumoDTO(4L, "Amoxicilina", new BigDecimal("2500"), 100)));
        when(facturaRepository.save(any(Factura.class))).thenAnswer(inv -> inv.getArgument(0));

        Factura factura = facturaService.emitir(request);

        assertEquals(0, factura.getTotal().compareTo(new BigDecimal("5000")));
        assertEquals(1, factura.getDetalles().size());
        // debe descontar el stock en inventario_service
        verify(insumoClient).descontarStock(4L, 2);
    }

    @Test
    void emitir_clienteInexistente_lanza404() {
        FacturaRequest request = new FacturaRequest(99L,
                List.of(new FacturaRequest.ItemRequest(4L, 2)));
        when(clienteClient.obtenerCliente(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facturaService.emitir(request));
        verify(facturaRepository, never()).save(any());
        verify(insumoClient, never()).descontarStock(anyLong(), anyInt());
    }

    @Test
    void emitir_insumoInexistente_lanza404() {
        FacturaRequest request = new FacturaRequest(3L,
                List.of(new FacturaRequest.ItemRequest(77L, 1)));
        when(clienteClient.obtenerCliente(3L))
                .thenReturn(Optional.of(new ClienteDTO(3L, "María", "González")));
        when(insumoClient.obtenerInsumo(77L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facturaService.emitir(request));
        verify(facturaRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(facturaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> facturaService.obtenerPorId(99L));
    }
}
