package com.ftec.repositories;

import com.ftec.entities.ConfirmData;
import com.ftec.resources.enums.ConfirmScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmDataDAO extends JpaRepository<ConfirmData, Long> {

    @Query(value = "select user_id from confirm_data where hash = ?1", nativeQuery = true)
    long findIdByHash(String hash);

    Optional<ConfirmData> findByHash(String hash);

    @Modifying
    void deleteByHash(String hash);

    @Modifying
    void deleteByHashAndScope(String hash, ConfirmScope restorePass);

    Optional<ConfirmData> findByUserIdAndScope(long id, ConfirmScope scope);

    @Modifying
    void deleteByUserIdAndScope(long userId, ConfirmScope confirmEmail);

    Optional<ConfirmData> findByHashAndScope(String hash, ConfirmScope confirmEmail);

    Optional<ConfirmData> findByUserId(long id);
}
