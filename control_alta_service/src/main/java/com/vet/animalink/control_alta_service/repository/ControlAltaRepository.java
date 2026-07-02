package com.vet.animalink.control_alta_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vet.animalink.control_alta_service.model.ControlAlta;
import com.vet.animalink.control_alta_service.model.enums.EstadoControl;

public interface ControlAltaRepository extends JpaRepository<ControlAlta, Long> {

    List<ControlAlta> findByMascotaId(Long mascotaId);

    List<ControlAlta> findByEstado(EstadoControl estado);


}
