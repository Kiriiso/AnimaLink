package com.vet.animalink.auth_service.service;

import com.vet.animalink.auth_service.dto.AuthResponse;
import com.vet.animalink.auth_service.dto.LoginRequest;
import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioAuthService {

    private final UsuarioAuthRepository usuarioAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioAuthService(UsuarioAuthRepository usuarioAuthRepository,
                               PasswordEncoder passwordEncoder,
                               JwtService jwtService) {
        this.usuarioAuthRepository = usuarioAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioAuthRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("El username ya existe");
        }

        UsuarioAuth usuario = UsuarioAuth.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol())
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