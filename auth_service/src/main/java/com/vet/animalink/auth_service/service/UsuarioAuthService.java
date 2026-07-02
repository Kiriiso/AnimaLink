package com.vet.animalink.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vet.animalink.auth_service.client.UsuarioClient;
import com.vet.animalink.auth_service.dto.AuthResponse;
import com.vet.animalink.auth_service.dto.LoginRequest;
import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;

@Service
public class UsuarioAuthService {

    private final UsuarioAuthRepository usuarioAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioClient usuarioClient;

    public UsuarioAuthService(UsuarioAuthRepository usuarioAuthRepository,
                               PasswordEncoder passwordEncoder,
                               JwtService jwtService,
                               UsuarioClient usuarioClient) {
        this.usuarioAuthRepository = usuarioAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.usuarioClient = usuarioClient;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioAuthRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("El username ya existe");
        }

        Long usuarioId; 
        try {
            usuarioId = usuarioClient.crearPerfil(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(
                "No se pudo crear el perfil del usuario. Intenta nuevamente.", e);
        }
        UsuarioAuth usuario = UsuarioAuth.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol())
                .usuarioId(usuarioId)
                .activo(true)
                .build();

        usuarioAuthRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, usuario.getUsername(), usuario.getRol().name());
    }

    public AuthResponse login(LoginRequest request) {
        UsuarioAuth usuario = usuarioAuthRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!usuario.isActivo()) {
            throw new IllegalArgumentException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, usuario.getUsername(), usuario.getRol().name());
    }
}