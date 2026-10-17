package com.techservices.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Error de negocio con su codigo HTTP (401, 403, 404, 429...).
 * Lo lanzan los servicios y lo convierte en JSON el GlobalExceptionHandler.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String mensaje) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
