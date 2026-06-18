package com.vet.users_service.service;

import com.vet.users_service.model.User;
import java.util.List;

public interface UserService {

    User save(User entity);

    User update(Long id, User entity);

    User findById(Long id);

    List<User> findAll();

    void deleteById(Long id);
}
