package com.techservices.api.repository;

import com.techservices.api.model.UsuarioApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CAPA REPOSITORIO: acceso a la tabla "usuarios_api".
 */
@Repository
public interface UsuarioApiRepository extends JpaRepository<UsuarioApi, Long> {

    /** Busca al usuario que tenga EXACTAMENTE esa API Key y ese Token. */
    Optional<UsuarioApi> findByApiKeyAndToken(String apiKey, String token);

    /**
     * Descuenta 1 token directamente en la BD, solo si aun le quedan tokens.
     * Hacerlo en un solo UPDATE evita que dos peticiones simultaneas
     * dejen el saldo en negativo. Devuelve cuantas filas se modificaron (1 o 0).
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE UsuarioApi u SET u.tokensDisponibles = u.tokensDisponibles - 1 "
         + "WHERE u.id = :id AND u.tokensDisponibles > 0")
    int descontarToken(@Param("id") Long id);
}
