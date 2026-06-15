package com.vet.pets_service.service;

import com.vet.pets_service.model.Mascota;

import java.util.List;

public interface MascotaService {

    Mascota save(Mascota mascota);

    Mascota findById(Long id);

    List<Mascota> findByClienteId(Long clienteId);

}
