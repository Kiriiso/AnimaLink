package com.vet.animalink.cliente_service.service;

import com.vet.animalink.cliente_service.exception.ResourceNotFoundException;
import com.vet.animalink.cliente_service.model.Cliente;
import com.vet.animalink.cliente_service.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .rut("18345678-9")
                .nombre("María")
                .apellido("González")
                .email("maria@correo.cl")
                .telefono("+56912345678")
                .direccion("Calle Falsa 123")
                .activo(true)
                .build();
    }

    @Test
    void listar_devuelveTodos() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<Cliente> lista = clienteService.listar();
        assertEquals(1, lista.size());
        assertEquals("María", lista.get(0).getNombre());
    }

    @Test
    void obtenerPorId_existente_devuelveCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Cliente encontrado = clienteService.obtenerPorId(1L);
        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.obtenerPorId(99L));
    }

    @Test
    void crear_nuevo_guarda() {
        when(clienteRepository.existsByRut(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente creado = clienteService.crear(cliente);
        assertEquals("González", creado.getApellido());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void crear_rutDuplicado_lanzaError() {
        when(clienteRepository.existsByRut("18345678-9")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> clienteService.crear(cliente));
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void eliminar_existente_borra() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);
        assertDoesNotThrow(() -> clienteService.eliminar(1L));
        verify(clienteRepository, times(1)).delete(cliente);
    }
}
