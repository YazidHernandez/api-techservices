package com.techservices.api.controller;

import com.techservices.api.dto.ApiResponse;
import com.techservices.api.model.Cliente;
import com.techservices.api.model.UsuarioApi;
import com.techservices.api.service.AutenticacionService;
import com.techservices.api.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CAPA CONTROLADOR: recibe las peticiones HTTP y devuelve JSON.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final AutenticacionService autenticacionService;

    public ClienteController(ClienteService clienteService, AutenticacionService autenticacionService) {
        this.clienteService = clienteService;
        this.autenticacionService = autenticacionService;
    }

    /**
     * GET /api/clientes/{id}
     * Headers: x-api-key y Authorization: Bearer <token>
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Cliente>> obtenerCliente(
            @PathVariable Long id,
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @RequestHeader(value = "Authorization", required = false) String authorization) {

        // 1. Seguridad: API Key + Token + estado + saldo
        UsuarioApi usuario = autenticacionService.validarCredenciales(apiKey, authorization);

        // 2. Consulta (si el cliente no existe se lanza 404 y NO se descuenta el token)
        Cliente cliente = clienteService.buscarPorId(id);

        // 3. Consulta exitosa -> se descuenta 1 token
        int tokensRestantes = autenticacionService.descontarToken(usuario);

        return ResponseEntity.ok(ApiResponse.ok("Consulta realizada correctamente", cliente, tokensRestantes));
    }
}
