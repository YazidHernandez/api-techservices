package com.techservices.api.service;

import com.techservices.api.exception.ApiException;
import com.techservices.api.model.UsuarioApi;
import com.techservices.api.repository.UsuarioApiRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CAPA SERVICIO: reglas de seguridad y control de consumo de tokens.
 */
@Service
public class AutenticacionService {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final UsuarioApiRepository usuarioApiRepository;

    public AutenticacionService(UsuarioApiRepository usuarioApiRepository) {
        this.usuarioApiRepository = usuarioApiRepository;
    }

    /**
     * Valida los headers x-api-key y Authorization.
     * Si algo falla lanza ApiException; si todo esta bien devuelve el usuario.
     */
    public UsuarioApi validarCredenciales(String apiKey, String authorization) {
        // 1. Los dos headers son obligatorios
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Falta el header obligatorio x-api-key");
        }
        if (authorization == null || !authorization.startsWith(PREFIJO_BEARER)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "El header Authorization es obligatorio y debe tener el formato: Bearer <token>");
        }
        String token = authorization.substring(PREFIJO_BEARER.length()).trim();

        // 2. La combinacion API Key + Token debe existir en la BD
        UsuarioApi usuario = usuarioApiRepository.findByApiKeyAndToken(apiKey.trim(), token)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "API Key o Token invalidos"));

        // 3. El usuario debe estar activo
        if (!usuario.estaActivo()) {
            throw new ApiException(HttpStatus.FORBIDDEN, "El usuario '" + usuario.getNombreUsuario() + "' esta inactivo");
        }

        // 4. Debe tener tokens disponibles
        if (usuario.getTokensDisponibles() <= 0) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,
                    "Has agotado tus tokens. Contacta al administrador para recargar tu saldo");
        }
        return usuario;
    }

    /**
     * Descuenta 1 token. Se llama SOLO despues de que la consulta fue exitosa.
     * @return tokens que le quedan al usuario
     */
    @Transactional
    public int descontarToken(UsuarioApi usuario) {
        int filasActualizadas = usuarioApiRepository.descontarToken(usuario.getId());
        if (filasActualizadas == 0) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,
                    "Has agotado tus tokens. Contacta al administrador para recargar tu saldo");
        }
        return usuario.getTokensDisponibles() - 1;
    }
}
