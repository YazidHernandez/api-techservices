package com.techservices.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Formato estandar de todas las respuestas JSON de la API.
 * Los campos null no se envian (por ejemplo, "data" en un error).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean exito,
        int status,
        String mensaje,
        T data,
        Integer tokensRestantes,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> ok(String mensaje, T data, Integer tokensRestantes) {
        return new ApiResponse<>(true, 200, mensaje, data, tokensRestantes, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status, String mensaje) {
        return new ApiResponse<>(false, status, mensaje, null, null, LocalDateTime.now());
    }
}
