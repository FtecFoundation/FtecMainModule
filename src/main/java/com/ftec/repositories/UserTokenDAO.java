package com.ftec.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftec.entities.UserToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


public interface UserTokenDAO extends ElasticsearchRepository<UserToken, String> {
	UserToken findByToken(String token);
}
