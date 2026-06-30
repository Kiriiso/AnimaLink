package com.vet.animalink.auth_service.service;

import com.vet.animalink.auth_service.dto.AuthResponse;
import com.vet.animalink.auth_service.dto.LoginRequest;
import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.model.Rol;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAuthServiceTest {

    @Mock
    private UsuarioAuthRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioAuthService service;

    @Test
    void register_nuevo_devuelveToken() {
        RegisterRequest req = new RegisterRequest("nuevo", "clave123", Rol.VETERINARIO);
        when(repository.existsByUsername("nuevo")).thenReturn(false);
        when(passwordEncoder.encode("clave123")).thenReturn("ENC");
        when(repository.save(any(UsuarioAuth.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtService.generateToken(any(UsuarioAuth.class))).thenReturn("jwt-token");

        AuthResponse res = service.register(req);

        assertEquals("jwt-token", res.token());
        assertEquals("nuevo", res.username());
        assertEquals("VETERINARIO", res.rol());
        // la contraseña debe guardarse ENCRIPTADA, no en texto plano
        verify(passwordEncoder).encode("clave123");
    }

    @Test
    void register_usernameExistente_lanzaError() {
        RegisterRequest req = new RegisterRequest("admin", "x", Rol.ADMIN);
        when(repository.existsByUsername("admin")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.register(req));
        verify(repository, never()).save(any());
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
