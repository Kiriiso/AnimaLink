package com.vet.animalink.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vet.animalink.auth_service.dto.AuthResponse;
import com.vet.animalink.auth_service.dto.LoginRequest;
import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.exception.GlobalExceptionHandler;
import com.vet.animalink.auth_service.model.Rol;
import com.vet.animalink.auth_service.service.UsuarioAuthService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioAuthService usuarioAuthService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        usuarioAuthController controller = new usuarioAuthController(usuarioAuthService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void register_valido_devuelve201ConToken() throws Exception {
        when(usuarioAuthService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthResponse("jwt-token", "nuevo", "VETERINARIO"));

        RegisterRequest req = new RegisterRequest("nuevo", "clave123", Rol.VETERINARIO);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
    }

    @Test
    void login_credencialesInvalidas_devuelve400() throws Exception {
        when(usuarioAuthService.login(any(LoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Credenciales invalidas"));

        LoginRequest req = new LoginRequest("admin", "malaclave");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Credenciales invalidas"));
    }
}
