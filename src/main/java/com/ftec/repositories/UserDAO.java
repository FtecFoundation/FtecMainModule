package com.ftec.repositories;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftec.entities.User;

public interface UserDAO extends ElasticsearchRepository<User, Long> {
	 
	 Optional<User> findById(long id);
	 Optional<User> findByUsername(String Username);
}
