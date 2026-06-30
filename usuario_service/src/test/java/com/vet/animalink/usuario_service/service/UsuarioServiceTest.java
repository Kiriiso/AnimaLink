package com.vet.animalink.usuario_service.service;

import com.vet.animalink.usuario_service.exception.ResourceNotFoundException;
import com.vet.animalink.usuario_service.model.Usuario;
import com.vet.animalink.usuario_service.model.enums.Rol;
import com.vet.animalink.usuario_service.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .rut("15987654-3")
                .nombre("Carlos")
                .apellido("Pérez")
                .email("carlos@animalink.cl")
                .telefono("+56998765432")
                .rol(Rol.VETERINARIO)
                .especialidad("Cirugía")
                .activo(true)
                .build();
    }

    @Test
    void obtenerPorId_existente_devuelveUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        assertEquals("Carlos", usuarioService.obtenerPorId(1L).getNombre());
    }

    @Test
    void obtenerPorId_inexistente_lanza404() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> usuarioService.obtenerPorId(99L));
    }

    @Test
    void crear_nuevo_guarda() {
        when(usuarioRepository.existsByRut(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario creado = usuarioService.crear(usuario);
        assertEquals(Rol.VETERINARIO, creado.getRol());
    }

    @Test
    void crear_emailDuplicado_lanzaError() {
        when(usuarioRepository.existsByRut(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail("carlos@animalink.cl")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> usuarioService.crear(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void listarPorRol_devuelveFiltrados() {
        when(usuarioRepository.findByRol(Rol.VETERINARIO)).thenReturn(List.of(usuario));
        List<Usuario> vets = usuarioService.listarPorRol(Rol.VETERINARIO);
        assertEquals(1, vets.size());
    }
}
