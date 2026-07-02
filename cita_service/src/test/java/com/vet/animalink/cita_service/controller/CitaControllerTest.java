package com.vet.animalink.cita_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.cita_service.client.dto.MascotaDTO;
import com.vet.animalink.cita_service.client.dto.UsuarioDTO;
import com.vet.animalink.cita_service.dto.CitaRequest;
import com.vet.animalink.cita_service.exception.GlobalExceptionHandler;
import com.vet.animalink.cita_service.exception.ResourceNotFoundException;
import com.vet.animalink.cita_service.mapper.CitaMapper;
import com.vet.animalink.cita_service.model.Cita;
import com.vet.animalink.cita_service.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CitaControllerTest {

    @Mock
    private CitaService citaService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Cita cita;

    @BeforeEach
    void setUp() {
        CitaMapper mapper = new CitaMapper();
        CitaController controller = new CitaController(citaService, mapper,
                new com.vet.animalink.cita_service.assembler.CitaModelAssembler());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        cita = Cita.builder()
                .id(1L).mascotaId(5L).veterinarioId(2L)
                .fechaHora(LocalDateTime.now().plusDays(2))
                .motivo("Vacunación")
                .build();
    }

    @Test
    void getPorId_existente_devuelve200ConNombres() throws Exception {
        when(citaService.obtenerPorId(1L)).thenReturn(cita);
        when(citaService.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));
        when(citaService.obtenerVeterinario(2L))
                .thenReturn(Optional.of(new UsuarioDTO(2L, "Carlos", "Pérez", "VETERINARIO")));

        mockMvc.perform(get("/api/v1/citas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mascotaNombre").value("Firulais"))
                .andExpect(jsonPath("$.data.veterinarioNombre").value("Carlos Pérez"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(citaService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Cita no encontrada con id 99"));

        mockMvc.perform(get("/api/v1/citas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_valido_devuelve201() throws Exception {
        when(citaService.crear(any(Cita.class))).thenReturn(cita);

        CitaRequest request = new CitaRequest(
                5L, 2L, LocalDateTime.now().plusDays(3), "Vacunación", "Sin observaciones");

        mockMvc.perform(post("/api/v1/citas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }
}
