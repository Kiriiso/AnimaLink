package com.vet.animalink.mascota_service.repository;

import com.vet.animalink.mascota_service.model.Mascota;
import com.vet.animalink.mascota_service.model.enums.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByClienteId(Long clienteId);

    List<Mascota> findByEspecie(Especie especie);

    List<Mascota> findByNombreContainingIgnoreCase(String nombre);

    List<Mascota> findByActivoTrue();
}
