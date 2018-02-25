package com.ftec.repositories.implementations;

import com.ftec.entities.User;
import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
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

    @Override
    public User getByLogin(String login) {
        return entityManager.createQuery("select u from User u where u.login=:login", User.class)
                .setParameter("login",login)
                .getSingleResult();
    }
}
