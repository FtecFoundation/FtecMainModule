package com.ftec.repositories.interfaces;

import com.ftec.entities.User;
import com.ftec.repositories.implementations.UserDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserDAOImpl implements UserDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(User u) {
        entityManager.persist(u);
    }

    @Override
    public void update(User u) {
        entityManager.merge(u);
    }

    @Override
    public void delete(User u) {
        entityManager.detach(u);
    }

    @Override
    public User getById(long id) {
        return entityManager.createQuery("select u from User u where u.id=:id", User.class)
                .setParameter("id",id)
                .getSingleResult();
    }
}
