package com.ftec.repositories;

import com.ftec.entities.UserToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface UserTokenDAO extends ElasticsearchRepository<UserToken, String> {
    Optional<UserToken> findByToken(String token);
    List<UserToken> removeByToken(String token);
}
