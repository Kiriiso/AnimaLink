package com.vet.auth_service.service;

import com.vet.auth_service.model.AuthUser;
import java.util.List;

public interface AuthUserService {

    AuthUser save(AuthUser entity);

    AuthUser update(Long id, AuthUser entity);

    AuthUser findById(Long id);

    List<AuthUser> findAll();

    void deleteById(Long id);
}
