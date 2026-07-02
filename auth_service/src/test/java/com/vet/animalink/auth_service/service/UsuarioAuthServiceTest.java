package com.vet.animalink.auth_service.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vet.animalink.auth_service.client.UsuarioClient;
import com.vet.animalink.auth_service.dto.AuthResponse;
import com.vet.animalink.auth_service.dto.LoginRequest;
import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.model.Rol;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioAuthServiceTest {

    @Mock
    private UsuarioAuthRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private UsuarioAuthService service;

    @Test
    void register_nuevo_devuelveToken() {
        RegisterRequest req = new RegisterRequest("nuevo", "clave123", Rol.VETERINARIO,
                "12345678-9", "Nombre", "Apellido", "nuevo@test.cl", "912345678", "General");

        when(repository.existsByUsername("nuevo")).thenReturn(false);
        when(usuarioClient.crearPerfil(req)).thenReturn(100L);
        when(passwordEncoder.encode("clave123")).thenReturn("ENC");
        when(repository.save(any(UsuarioAuth.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtService.generateToken(any(UsuarioAuth.class))).thenReturn("jwt-token");

        AuthResponse res = service.register(req);

        assertEquals("jwt-token", res.token());
        assertEquals("nuevo", res.username());
        assertEquals("VETERINARIO", res.rol());
        verify(passwordEncoder).encode("clave123");
        verify(usuarioClient).crearPerfil(req);
    }

    @Test
    void register_usernameExistente_lanzaError() {
        RegisterRequest req = new RegisterRequest("admin", "x", Rol.ADMIN,
                "87654321-0", "Admin", "Uno", "admin@test.cl", null, null);

        when(repository.existsByUsername("admin")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.register(req));
        verify(repository, never()).save(any());
        verify(usuarioClient, never()).crearPerfil(any());
    }

    @Test
    void login_credencialesValidas_devuelveToken() {
        UsuarioAuth usuario = UsuarioAuth.builder()
                .id(1L).username("admin").password("ENC").rol(Rol.ADMIN).activo(true).build();
        when(repository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("admin123", "ENC")).thenReturn(true);
        when(jwtService.generateToken(usuario)).thenReturn("jwt-token");

        AuthResponse res = service.login(new LoginRequest("admin", "admin123"));
        assertEquals("jwt-token", res.token());
        assertEquals("ADMIN", res.rol());
    }

    @Test
    void login_usuarioInexistente_lanzaError() {
        when(repository.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.login(new LoginRequest("ghost", "x")));
    }

    @Test
    void login_passwordIncorrecta_lanzaError() {
        UsuarioAuth usuario = UsuarioAuth.builder()
                .id(1L).username("admin").password("ENC").rol(Rol.ADMIN).activo(true).build();
        when(repository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> service.login(new LoginRequest("admin", "malaclave")));
    }
}