package com.ftec.services.interfaces;

import com.ftec.entities.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getById(long id);

    Optional<User> findById(Long id);
}