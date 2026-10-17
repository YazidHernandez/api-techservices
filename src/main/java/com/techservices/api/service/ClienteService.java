package com.techservices.api.service;

import com.techservices.api.exception.ApiException;
import com.techservices.api.model.Cliente;
import com.techservices.api.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * CAPA SERVICIO: logica de negocio de clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "No existe un cliente con ID " + id));
    }
}
