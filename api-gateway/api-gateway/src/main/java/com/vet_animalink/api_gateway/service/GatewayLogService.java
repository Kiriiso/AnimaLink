package com.vet_animalink.api_gateway.service;

import com.vet_animalink.api_gateway.model.GatewayLog;
import java.util.List;

public interface GatewayLogService {

    GatewayLog save(GatewayLog entity);

    GatewayLog update(Long id, GatewayLog entity);

    GatewayLog findById(Long id);

    List<GatewayLog> findAll();

    void deleteById(Long id);
}
