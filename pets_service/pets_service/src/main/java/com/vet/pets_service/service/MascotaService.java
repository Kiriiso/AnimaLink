package com.vet.pets_service.service;

import com.vet.pets_service.model.Mascota;
import java.util.List;

public interface MascotaService {

    Mascota save(Mascota entity);

    Mascota update(Long id, Mascota entity);

    Mascota findById(Long id);

    List<Mascota> findAll();

    void deleteById(Long id);
}
