package com.ftec.repositories;

import com.ftec.entities.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends ElasticsearchRepository<User, Long> {
    @Override
    Optional<User> findById(Long id);

    List<User> findAll();

    Optional<User> findByName(String name);
}
