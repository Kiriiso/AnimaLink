package com.vet.animalink.control_alta_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.control_alta_service.client.dto.MascotaDTO;
import com.vet.animalink.control_alta_service.dto.ControlAltaRequest;
import com.vet.animalink.control_alta_service.exception.GlobalExceptionHandler;
import com.vet.animalink.control_alta_service.exception.ResourceNotFoundException;
import com.vet.animalink.control_alta_service.mapper.ControlAltaMapper;
import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import com.vet.animalink.control_alta_service.service.ControlAltaService;
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
class ControlAltaControllerTest {

    @Mock
    private ControlAltaService controlAltaService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private ControlAlta control;

    @BeforeEach
    void setUp() {
        ControlAltaMapper mapper = new ControlAltaMapper();
        ControlAltaController controller = new ControlAltaController(controlAltaService, mapper,
                new com.vet.animalink.control_alta_service.assembler.ControlAltaModelAssembler());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        control = ControlAlta.builder()
                .id(1L).mascotaId(5L)
                .fechaIngreso(LocalDateTime.now())
                .motivoIngreso("Post-operatorio")
                .estado(EstadoControl.HOSPITALIZADO)
                .build();
    }

    @Test
    void getPorId_existente_devuelve200ConMascota() throws Exception {
        when(controlAltaService.obtenerPorId(1L)).thenReturn(control);
        when(controlAltaService.obtenerMascota(5L))
                .thenReturn(Optional.of(new MascotaDTO(5L, "Firulais", "PERRO")));

        mockMvc.perform(get("/api/v1/controles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.estado").value("HOSPITALIZADO"))
                .andExpect(jsonPath("$.data.mascotaNombre").value("Firulais"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(controlAltaService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Control no encontrado con id 99"));

        mockMvc.perform(get("/api/v1/controles/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void registrarIngreso_valido_devuelve201() throws Exception {
        when(controlAltaService.registrarIngreso(any(ControlAlta.class))).thenReturn(control);

        ControlAltaRequest request = new ControlAltaRequest(5L, "Post-operatorio", "Observación 48h");

        mockMvc.perform(post("/api/v1/controles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }
}
