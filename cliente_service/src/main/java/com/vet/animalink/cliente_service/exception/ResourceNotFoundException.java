package com.vet.animalink.cliente_service.exception;

/**
 * Se lanza cuando no se encuentra un recurso. El GlobalExceptionHandler
 * la traduce a una respuesta 404 con cuerpo personalizado.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
