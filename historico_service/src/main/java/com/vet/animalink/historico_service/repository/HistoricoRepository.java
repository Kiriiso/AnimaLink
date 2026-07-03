package com.vet.animalink.historico_service.repository;

import com.vet.animalink.historico_service.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByMetodoPago(String metodoPago);

    List<Historico> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
}