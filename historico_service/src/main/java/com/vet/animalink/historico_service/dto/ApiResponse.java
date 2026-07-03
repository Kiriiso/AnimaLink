package com.vet.animalink.historico_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta estándar del API para el servicio de históricos")
public record ApiResponse<T>(
        int status,
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> of(int status, boolean success, String message, T data) {
        return new ApiResponse<>(status, success, message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return of(200, true, message, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return of(201, true, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return of(status, false, message, null);
    }
}