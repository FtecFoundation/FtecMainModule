package com.ftec.repositories;

import com.ftec.entities.TokenEmbId;
import com.ftec.entities.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TokenDAO extends CrudRepository<Token, TokenEmbId> {
	Optional<Token> findByToken(String token);
	@Transactional
	void deleteByToken(String token);
}
