package com.vet.animalink.cliente_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.cliente_service.assembler.ClienteModelAssembler;
import com.vet.animalink.cliente_service.dto.ClienteRequest;
import com.vet.animalink.cliente_service.exception.GlobalExceptionHandler;
import com.vet.animalink.cliente_service.exception.ResourceNotFoundException;
import com.vet.animalink.cliente_service.mapper.ClienteMapper;
import com.vet.animalink.cliente_service.model.Cliente;
import com.vet.animalink.cliente_service.service.ClienteService;
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
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        ClienteMapper mapper = new ClienteMapper();
        ClienteController controller = new ClienteController(clienteService, mapper, new ClienteModelAssembler());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

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
    void getPorId_existente_devuelve200() throws Exception {
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("María"))
                // el RUT debe viajar ENMASCARADO
                .andExpect(jsonPath("$.data.rut").value("********-9"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(clienteService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado con id 99"));

        mockMvc.perform(get("/api/v1/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cliente no encontrado con id 99"));
    }

    @Test
    void crear_valido_devuelve201() throws Exception {
        when(clienteService.crear(any(Cliente.class))).thenReturn(cliente);

        ClienteRequest request = new ClienteRequest(
                "18345678-9", "María", "González",
                "maria@correo.cl", "+56912345678", "Calle Falsa 123");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Cliente creado correctamente"));
    }

    @Test
    void crear_invalido_devuelve400() throws Exception {
        // RUT y email vacíos -> debe fallar la validación del DTO
        ClienteRequest request = new ClienteRequest(
                "", "María", "González", "no-es-email", "", "");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
