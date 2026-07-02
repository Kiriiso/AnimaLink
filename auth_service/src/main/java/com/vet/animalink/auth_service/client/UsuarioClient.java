package com.vet.animalink.auth_service.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.vet.animalink.auth_service.dto.RegisterRequest;
import com.vet.animalink.auth_service.model.UsuarioAuth;
import com.vet.animalink.auth_service.repository.UsuarioAuthRepository;
import com.vet.animalink.auth_service.service.JwtService;

@Component
public class UsuarioClient {

    private final RestClient restClient;
    private final UsuarioAuthRepository usuarioAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String serviceUsername;
    private final String servicePassword;

    public UsuarioClient(@Value("${usuario.service.url}") String usuarioServiceUrl,
                          @Value("${service-account.username}") String serviceUsername,
                          @Value("${service-account.password}") String servicePassword,
                          UsuarioAuthRepository usuarioAuthRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.restClient = RestClient.builder().baseUrl(usuarioServiceUrl).build();
        this.usuarioAuthRepository = usuarioAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.serviceUsername = serviceUsername;
        this.servicePassword = servicePassword;
    }

    public Long crearPerfil(RegisterRequest request) {
        String tokenServicio = loginServicio();

        Map<String, Object> body = Map.of(
                "rut", request.rut(),
                "nombre", request.nombre(),
                "apellido", request.apellido(),
                "email", request.email(),
                "telefono", request.telefono() == null ? "" : request.telefono(),
                "rol", request.rol().name(),
                "especialidad", request.especialidad() == null ? "N/A" : request.especialidad()
        );

        Map<String, Object> response = restClient.post()
                .uri("/api/v1/usuarios")
                .header("Authorization", "Bearer " + tokenServicio)
                .body(body)
                .retrieve()
                .body(Map.class);

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return Long.valueOf(data.get("id").toString());
    }

    private String loginServicio() {
        UsuarioAuth usuario = usuarioAuthRepository.findByUsername(serviceUsername)
                .orElseThrow(() -> new IllegalStateException(
                        "Cuenta de servicio '" + serviceUsername + "' no existe en auth_service"));

        if (!passwordEncoder.matches(servicePassword, usuario.getPassword())) {
            throw new IllegalStateException("Credenciales de la cuenta de servicio son inválidas");
        }

        return jwtService.generateToken(usuario);
    }
}