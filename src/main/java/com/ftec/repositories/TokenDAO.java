package com.ftec.repositories;

import com.ftec.entities.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenDAO extends CrudRepository<Token, String> {
	Optional<Token> findByToken(String token);//TODO try optimize
	@Modifying
	@Transactional //TODO delete when migrate in test from TokenDAO to TokenServoce
	void deleteByToken(String token);

	@Query(value = "DELETE FROM token_table where token in (select * from" +
            " (SELECT token_table.token FROM token_table where token_table.user_id = ?1 order by token_table.expiration_time LIMIT 9999 OFFSET 5) as t);",nativeQuery = true)
	@Modifying
    void deleteExcessiveToken(long userId);


    @Query(value = "DELETE FROM token_table where token in" +
            " (select * from (select token_table.token FROM token_table where token_table.expiration_time < CURDATE()) as t)",nativeQuery = true)
    @Modifying
    void deleteAllExpiredToken();

    List<Token> findAllByUserId(long userId);//only for test purposes
}
