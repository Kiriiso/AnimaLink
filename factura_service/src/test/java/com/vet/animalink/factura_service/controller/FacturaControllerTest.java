package com.vet.animalink.factura_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.factura_service.client.dto.ClienteDTO;
import com.vet.animalink.factura_service.dto.FacturaRequest;
import com.vet.animalink.factura_service.exception.GlobalExceptionHandler;
import com.vet.animalink.factura_service.exception.ResourceNotFoundException;
import com.vet.animalink.factura_service.mapper.FacturaMapper;
import com.vet.animalink.factura_service.model.DetalleFactura;
import com.vet.animalink.factura_service.model.Factura;
import com.vet.animalink.factura_service.model.enums.EstadoFactura;
import com.vet.animalink.factura_service.service.FacturaService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FacturaControllerTest {

    @Mock
    private FacturaService facturaService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Factura factura;

    @BeforeEach
    void setUp() {
        FacturaMapper mapper = new FacturaMapper();
        FacturaController controller = new FacturaController(facturaService, mapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        DetalleFactura detalle = DetalleFactura.builder()
                .id(1L).insumoId(4L).descripcion("Amoxicilina")
                .cantidad(2).precioUnitario(new BigDecimal("2500")).subtotal(new BigDecimal("5000"))
                .build();
        factura = Factura.builder()
                .id(1L).clienteId(3L).fecha(LocalDateTime.now())
                .estado(EstadoFactura.EMITIDA).total(new BigDecimal("5000"))
                .detalles(new java.util.ArrayList<>(List.of(detalle)))
                .build();
    }

    @Test
    void emitir_valido_devuelve201() throws Exception {
        when(facturaService.emitir(any(FacturaRequest.class))).thenReturn(factura);

        FacturaRequest request = new FacturaRequest(3L,
                List.of(new FacturaRequest.ItemRequest(4L, 2)));

        mockMvc.perform(post("/api/v1/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.total").value(5000));
    }

    @Test
    void getPorId_existente_devuelve200ConCliente() throws Exception {
        when(facturaService.obtenerPorId(1L)).thenReturn(factura);
        when(facturaService.obtenerCliente(3L))
                .thenReturn(Optional.of(new ClienteDTO(3L, "María", "González")));

        mockMvc.perform(get("/api/v1/facturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.clienteNombre").value("María González"))
                .andExpect(jsonPath("$.data.items[0].descripcion").value("Amoxicilina"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(facturaService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Factura no encontrada con id 99"));

        mockMvc.perform(get("/api/v1/facturas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
