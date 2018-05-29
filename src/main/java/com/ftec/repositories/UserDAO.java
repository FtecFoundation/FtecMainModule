package com.ftec.repositories;

import com.ftec.entities.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserDAO extends ElasticsearchRepository<User, Long> {

    @Override
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String Username);
}
