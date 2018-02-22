package com.ftec.repositories.implementations;

import com.ftec.entities.User;

public interface UserDAO {
    void persist(User u);
    void update(User u);
    void delete(User u);
    User getById(long id);
}
