package com.vet.animalink.inventario_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.inventario_service.dto.InsumoRequest;
import com.vet.animalink.inventario_service.exception.GlobalExceptionHandler;
import com.vet.animalink.inventario_service.exception.ResourceNotFoundException;
import com.vet.animalink.inventario_service.mapper.InsumoMapper;
import com.vet.animalink.inventario_service.model.Insumo;
import com.vet.animalink.inventario_service.model.enums.Categoria;
import com.vet.animalink.inventario_service.service.InsumoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InsumoControllerTest {

    @Mock
    private InsumoService insumoService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Insumo insumo;

    @BeforeEach
    void setUp() {
        InsumoMapper mapper = new InsumoMapper();
        InsumoController controller = new InsumoController(insumoService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        insumo = Insumo.builder()
                .id(1L).nombre("Amoxicilina 500mg").categoria(Categoria.MEDICAMENTO)
                .stock(10).stockMinimo(5)
                .precioCosto(new BigDecimal("1500"))
                .precioVenta(new BigDecimal("2500"))
                .build();
    }

    @Test
    void getPorId_existente_devuelve200SinPrecioCosto() throws Exception {
        when(insumoService.obtenerPorId(1L)).thenReturn(insumo);

        mockMvc.perform(get("/api/v1/insumos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("Amoxicilina 500mg"))
                .andExpect(jsonPath("$.data.precioVenta").value(2500))
                // el precio de COSTO es sensible y NO debe aparecer en la respuesta
                .andExpect(jsonPath("$.data.precioCosto").doesNotExist());
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(insumoService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Insumo no encontrado con id 99"));

        mockMvc.perform(get("/api/v1/insumos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void crear_valido_devuelve201() throws Exception {
        when(insumoService.crear(any(Insumo.class))).thenReturn(insumo);

        InsumoRequest request = new InsumoRequest(
                "Amoxicilina 500mg", Categoria.MEDICAMENTO, "Antibiótico",
                10, 5, new BigDecimal("1500"), new BigDecimal("2500"), null);

        mockMvc.perform(post("/api/v1/insumos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }
}
