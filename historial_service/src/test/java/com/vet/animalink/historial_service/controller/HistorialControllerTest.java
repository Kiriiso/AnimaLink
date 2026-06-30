package com.vet.animalink.historial_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.historial_service.client.dto.MascotaDTO;
import com.vet.animalink.historial_service.dto.HistorialRequest;
import com.vet.animalink.historial_service.exception.GlobalExceptionHandler;
import com.vet.animalink.historial_service.exception.ResourceNotFoundException;
import com.vet.animalink.historial_service.mapper.HistorialMapper;
import com.vet.animalink.historial_service.model.Historial;
import com.vet.animalink.historial_service.service.HistorialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HistorialControllerTest {

    @Mock
    private HistorialService historialService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Historial historial;

    @BeforeEach
    void setUp() {
        HistorialMapper mapper = new HistorialMapper();
        HistorialController controller = new HistorialController(historialService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        historial = Historial.builder()
                .id(1L).mascotaId(5L)
                .fecha(LocalDate.now().minusDays(3))
                .diagnostico("Otitis externa")
                .tratamiento("Antibiótico tópico")
                .build();
    }

    @Test
    void getPorId_existente_devuelve200ConMascota() throws Exception {
        when(historialService.obtenerPorId(1L)).thenReturn(historial);
        when(historialService.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));

        mockMvc.perform(get("/api/v1/historiales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.diagnostico").value("Otitis externa"))
                .andExpect(jsonPath("$.data.mascotaNombre").value("Firulais"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(historialService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Historial no encontrado con id 99"));

        mockMvc.perform(get("/api/v1/historiales/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_valido_devuelve201() throws Exception {
        when(historialService.crear(any(Historial.class))).thenReturn(historial);

        HistorialRequest request = new HistorialRequest(
                5L, LocalDate.now().minusDays(1), "Otitis externa", "Antibiótico", "Controlar en 10 días");

        mockMvc.perform(post("/api/v1/historiales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }
}
