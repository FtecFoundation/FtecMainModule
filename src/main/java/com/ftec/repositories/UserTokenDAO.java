package com.ftec.repositories;

import com.ftec.entities.TokenEmbId;
import com.ftec.entities.UserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserTokenDAO extends CrudRepository<UserToken, TokenEmbId> {
	Optional<UserToken> findByIdToken(String token);
	@Transactional
	void deleteByIdToken(String token);
}
