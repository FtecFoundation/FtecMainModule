package com.ftec.repositories;

import com.ftec.entities.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserDAO extends ElasticsearchRepository<User, Long> {
}
