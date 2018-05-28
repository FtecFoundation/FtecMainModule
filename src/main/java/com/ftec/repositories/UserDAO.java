package com.ftec.repositories;

import com.ftec.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO {

    User findById(long id);

}
