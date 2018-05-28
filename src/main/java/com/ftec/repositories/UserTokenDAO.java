package com.ftec.repositories;

import com.ftec.entities.UserToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserTokenDAO extends ElasticsearchRepository<UserToken, String> {
	UserToken getByToken(String token);
}
