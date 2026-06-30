package com.vet.animalink.usuario_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.usuario_service.dto.UsuarioRequest;
import com.vet.animalink.usuario_service.exception.GlobalExceptionHandler;
import com.vet.animalink.usuario_service.exception.ResourceNotFoundException;
import com.vet.animalink.usuario_service.mapper.UsuarioMapper;
import com.vet.animalink.usuario_service.model.Usuario;
import com.vet.animalink.usuario_service.model.enums.Rol;
import com.vet.animalink.usuario_service.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        UsuarioMapper mapper = new UsuarioMapper();
        UsuarioController controller = new UsuarioController(usuarioService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

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
    void getPorId_existente_devuelve200ConRutEnmascarado() throws Exception {
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("Carlos"))
                .andExpect(jsonPath("$.data.rol").value("VETERINARIO"))
                .andExpect(jsonPath("$.data.rut").value("********-3"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(usuarioService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado con id 99"));

        mockMvc.perform(get("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_valido_devuelve201() throws Exception {
        when(usuarioService.crear(any(Usuario.class))).thenReturn(usuario);

        UsuarioRequest request = new UsuarioRequest(
                "15987654-3", "Carlos", "Pérez", "carlos@animalink.cl",
                "+56998765432", Rol.VETERINARIO, "Cirugía");

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }
}
