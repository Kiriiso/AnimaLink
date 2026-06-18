package com.vet.cliente_service.service;

import com.vet.cliente_service.model.Cliente;
import java.util.List;

public interface ClienteService {

    Cliente save(Cliente entity);

    Cliente update(Long id, Cliente entity);

    Cliente findById(Long id);

    List<Cliente> findAll();

    void deleteById(Long id);
}
