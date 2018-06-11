package com.ftec.repositories;

import com.ftec.entities.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenDAO extends CrudRepository<Token, String> {
	Optional<Token> findByToken(String token);

	@Modifying
	void deleteByToken(String token);

    @Modifying
    @Query(value = "DELETE FROM token_table where token in (select * from" +
            " (SELECT token_table.token FROM token_table where token_table.user_id = ?1 order by token_table.expiration_time LIMIT 9999 OFFSET 5) as t);",nativeQuery = true)
    void deleteExcessiveToken(long userId);

    @Modifying
    @Query(value = "DELETE FROM token_table where token_table.expiration_time < CURDATE()",nativeQuery = true)

    void deleteAllExpiredToken();

    List<Token> findAllByUserId(long userId);//only for test purposes

    @Modifying
    @Query(value = "UPDATE Token t set t.expirationTime = ?1 where t.token = ?2")
    void updateExpirationDate(Date newDate, String token);

    @Modifying
    void deleteByUserId(long userId);

    @Query(value = "select * from token_table", nativeQuery = true)
    List<Token> getAll();
}
