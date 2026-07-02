package com.vet.animalink.cita_service.repository;

import com.vet.animalink.cita_service.model.Cita;
import com.vet.animalink.cita_service.model.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByMascotaId(Long mascotaId);

    List<Cita> findByVeterinarioId(Long veterinarioId);

    List<Cita> findByEstado(EstadoCita estado);
}
