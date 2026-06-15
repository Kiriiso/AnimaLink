package com.vet.cliente_service.service;

import com.vet.cliente_service.model.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente save(Cliente cliente);

    Cliente findById(Long id);

    List<Cliente> findAll();

}
