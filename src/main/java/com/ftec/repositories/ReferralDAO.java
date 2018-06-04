package com.ftec.repositories;

import com.ftec.entities.ReferralSystem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralDAO<T extends ReferralSystem> extends CrudRepository<T, Long> {

    @Query("SELECT SUM(balance) " +
            "FROM (SELECT * FROM ReferralLevelOne one " +
            "UNION SELECT * FROM ReferralLevelTwo two " +
            "UNION SELECT * FROM ReferralLevelThree three)" +
            "WHERE user = ?")
    double findTotalBalance(@Param("user") long user);
}
