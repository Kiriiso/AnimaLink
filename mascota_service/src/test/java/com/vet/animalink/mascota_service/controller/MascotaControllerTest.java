package com.vet.animalink.mascota_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.mascota_service.client.dto.ClienteDTO;
import com.vet.animalink.mascota_service.dto.MascotaRequest;
import com.vet.animalink.mascota_service.exception.GlobalExceptionHandler;
import com.vet.animalink.mascota_service.exception.ResourceNotFoundException;
import com.vet.animalink.mascota_service.mapper.MascotaMapper;
import com.vet.animalink.mascota_service.model.Mascota;
import com.vet.animalink.mascota_service.model.enums.Especie;
import com.vet.animalink.mascota_service.model.enums.Sexo;
import com.vet.animalink.mascota_service.service.MascotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MascotaControllerTest {

    @Mock
    private MascotaService mascotaService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private Mascota mascota;

    @BeforeEach
    void setUp() {
        MascotaMapper mapper = new MascotaMapper();
        MascotaController controller = new MascotaController(mascotaService, mapper,
                new com.vet.animalink.mascota_service.assembler.MascotaModelAssembler());
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mascota = Mascota.builder()
                .id(1L)
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .raza("Labrador")
                .sexo(Sexo.MACHO)
                .color("Café")
                .clienteId(3L)
                .activo(true)
                .build();
    }

    @Test
    void getPorId_existente_devuelve200ConDueno() throws Exception {
        when(mascotaService.obtenerPorId(1L)).thenReturn(mascota);
        when(mascotaService.obtenerDueno(3L))
                .thenReturn(Optional.of(new ClienteDTO(3L, "María", "González")));

        mockMvc.perform(get("/api/v1/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.nombre").value("Firulais"))
                .andExpect(jsonPath("$.data.duenoNombre").value("María González"));
    }

    @Test
    void getPorId_inexistente_devuelve404() throws Exception {
        when(mascotaService.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Mascota no encontrada con id 99"));

        mockMvc.perform(get("/api/v1/mascotas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Mascota no encontrada con id 99"));
    }

    @Test
    void crear_valido_devuelve201() throws Exception {
        when(mascotaService.crear(any(Mascota.class))).thenReturn(mascota);

        MascotaRequest request = new MascotaRequest(
                "Firulais", Especie.PERRO, "Labrador", Sexo.MACHO, null, "Café", 3L);

        mockMvc.perform(post("/api/v1/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    void crear_invalido_devuelve400() throws Exception {
        // nombre vacío y especie nula -> falla validación
        MascotaRequest request = new MascotaRequest(
                "", null, "Labrador", Sexo.MACHO, null, "Café", 3L);

        mockMvc.perform(post("/api/v1/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
