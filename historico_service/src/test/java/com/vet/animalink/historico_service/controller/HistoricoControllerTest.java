package com.vet.animalink.historico_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.historico_service.dto.HistoricoRequest;
import com.vet.animalink.historico_service.exception.GlobalExceptionHandler;
import com.vet.animalink.historico_service.exception.ResourceNotFoundException;
import com.vet.animalink.historico_service.mapper.HistoricoMapper;
import com.vet.animalink.historico_service.model.Historico;
import com.vet.animalink.historico_service.service.HistoricoService;
import com.vet.animalink.historico_service.assembler.HistoricoModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HistoricoControllerTest {

    @Mock
    private HistoricoService historicoService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Historico historico;

    @BeforeEach
    void setUp() {
        HistoricoMapper mapper = new HistoricoMapper();
        HistoricoController controller = new HistoricoController(historicoService, mapper, new HistoricoModelAssembler());
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        historico = Historico.builder()
                .id(1L)
                .totalNeto(new BigDecimal("10000.00"))
                .descuento(new BigDecimal("500.00"))
                .iva(new BigDecimal("1900.00"))
                .totalVenta(new BigDecimal("11400.00"))
                .metodoPago("EFECTIVO")
                .fechaVenta(LocalDateTime.now())
                .build();
    }

    @Test
    void registrar_valido_devuelve201() throws Exception {
        when(historicoService.registrar(any(HistoricoRequest.class))).thenReturn(historico);

        HistoricoRequest request = new HistoricoRequest(
                new BigDecimal("10000.00"),
                new BigDecimal("500.00"),
                new BigDecimal("1900.00"),
                new BigDecimal("11400.00"),
                "EFECTIVO",
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/v1/historicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.totalVenta").value(11400.00))
                .andExpect(jsonPath("$.data.metodoPago").value("EFECTIVO"));
    }

    @Test
    void getPorId_existente_devuelve200() throws Exception {
        when(historicoService.obtenerPorId(1L)).thenReturn(historico);

        mockMvc.perform(get("/api/v1/historicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.totalVenta").value(11400.00))
                .andExpect(jsonPath("$.data.metodoPago").value("EFECTIVO"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(historicoService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Registro histórico no encontrado con id 99"));

        mockMvc.perform(get("/api/v1/historicos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}