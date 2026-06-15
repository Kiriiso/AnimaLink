package com.vet.pets_service.service.impl;

import com.vet.pets_service.model.Mascota;
import com.vet.pets_service.repository.MascotaRepository;
import com.vet.pets_service.service.MascotaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository repository;

    public MascotaServiceImpl(MascotaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mascota save(Mascota mascota) {
        return repository.save(mascota);
    }

    @Override
    public Mascota findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Mascota> findByClienteId(Long clienteId) {
        return repository.findByClienteId(clienteId);
    }
}
