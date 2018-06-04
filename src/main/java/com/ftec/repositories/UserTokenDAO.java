package com.ftec.repositories;

import com.ftec.entities.UserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserTokenDAO extends CrudRepository<UserToken, String> {
	Optional<UserToken> findByToken(String token);
	@Transactional
	void deleteByToken(String token);
}
