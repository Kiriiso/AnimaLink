package com.vet.animalink.cliente_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Envoltorio de respuesta ESTÁNDAR para personalizar el cuerpo de cada código HTTP
 * (200 OK, 201 CREATED, 404 NOT FOUND, etc.) de forma uniforme en todo el microservicio.
 */
@Schema(description = "Respuesta estándar del API con metadatos del resultado")
public record ApiResponse<T>(

        @Schema(description = "Código HTTP", example = "200")
        int status,

        @Schema(description = "Indica si la operación fue exitosa", example = "true")
        boolean success,

        @Schema(description = "Mensaje descriptivo del resultado", example = "Cliente encontrado correctamente")
        String message,

        @Schema(description = "Datos devueltos (puede ser null en errores)")
        T data,

        @Schema(description = "Marca de tiempo de la respuesta")
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> of(int status, boolean success, String message, T data) {
        return new ApiResponse<>(status, success, message, data, LocalDateTime.now());
    }

    /** 200 OK con datos. */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return of(200, true, message, data);
    }

    /** 201 CREATED con datos. */
    public static <T> ApiResponse<T> created(String message, T data) {
        return of(201, true, message, data);
    }

    /** Respuesta de error (404, 400, 409, 500...) sin datos. */
    public static <T> ApiResponse<T> error(int status, String message) {
        return of(status, false, message, null);
    }
}
