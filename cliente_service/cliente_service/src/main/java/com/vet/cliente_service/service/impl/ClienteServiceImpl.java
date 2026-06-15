package com.vet.cliente_service.service.impl;

import com.vet.cliente_service.model.Cliente;
import com.vet.cliente_service.repository.ClienteRepository;
import com.vet.cliente_service.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    public ClienteServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Cliente findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Cliente> findAll() {
        return repository.findAll();
    }
}
