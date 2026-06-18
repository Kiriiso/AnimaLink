package com.vet_animalink.api_gateway.repository;

import com.vet_animalink.api_gateway.model.GatewayLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayLogRepository extends JpaRepository<GatewayLog, Long> {
}
