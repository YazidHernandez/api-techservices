package com.techservices.api.repository;

import com.techservices.api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA REPOSITORIO: acceso a la tabla "clientes".
 * JpaRepository ya trae findById, findAll, save, deleteById, etc.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
