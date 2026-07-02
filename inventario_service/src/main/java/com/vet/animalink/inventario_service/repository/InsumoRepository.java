package com.vet.animalink.inventario_service.repository;

import com.vet.animalink.inventario_service.model.Insumo;
import com.vet.animalink.inventario_service.model.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    List<Insumo> findByCategoria(Categoria categoria);

    List<Insumo> findByNombreContainingIgnoreCase(String nombre);

    /** Insumos cuyo stock está en o por debajo del mínimo. */
    @Query("SELECT i FROM Insumo i WHERE i.stock <= i.stockMinimo")
    List<Insumo> findBajoStock();
}
