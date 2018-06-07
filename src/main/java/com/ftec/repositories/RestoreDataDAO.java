package com.ftec.repositories;

import com.ftec.entities.RestoreData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RestoreDataDAO extends JpaRepository<RestoreData, Long> {

    @Query(value = "select user_id from restore_data where hash = ?1", nativeQuery = true)
    long findIdByHash(String hash);

    Optional<RestoreData> findByHash(String hash);

    @Modifying
    @Transactional
    void deleteByHash(String hash);
}
