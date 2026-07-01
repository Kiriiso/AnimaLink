package com.vet.animalink.cita_service.config;

import com.vet.animalink.cita_service.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Seguridad por roles:
 *  - Swagger y lecturas (GET) son públicas (permite además las validaciones entre microservicios).
 *  - Las escrituras (POST/PUT/PATCH/DELETE) requieren un JWT válido con uno de los
 *    roles indicados en la propiedad app.security.write-roles (distinto por servicio).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Value("${app.security.write-roles}")
    private String[] writeRoles;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/doc/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers("/api/**").hasAnyRole(writeRoles)
                .anyRequest().permitAll())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
