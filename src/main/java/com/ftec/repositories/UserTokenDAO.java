package com.ftec.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftec.entities.UserToken;

public interface UserTokenDAO extends ElasticsearchRepository<UserToken, String> {
	UserToken findByToken(String token);
	void deleteByToken(String token);
}
