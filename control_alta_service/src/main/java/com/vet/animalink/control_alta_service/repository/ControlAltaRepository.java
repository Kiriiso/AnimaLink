package com.vet.animalink.control_alta_service.repository;

import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ControlAltaRepository extends JpaRepository<ControlAlta, Long> {

    List<ControlAlta> findByMascotaId(Long mascotaId);

    List<ControlAlta> findByEstado(EstadoControl estado);
}
