package com.ftec.repositories.implementations;

import com.ftec.configs.security.CustomUserDetails;
import com.ftec.entities.User;
import com.ftec.repositories.interfaces.UserDAO;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public User getAuthenticatedUser() {
        return getById(getAuthenticatedUserId());
    }

    @Override
    public void updateUserLanguage(String newLang, String user) {
        //TODO implement feature
    }

    @Override
    public boolean deleteUser(long userId) {
        try {
            return entityManager.createQuery("delete from User where id=:id")
                    .setParameter("id", userId)
                    .executeUpdate() == 1;
        }catch (Exception e){
            return false;
        }
    }

    //TODO
    @Override
    public User getUserByChatId(Long chatId) {
        return null;
    }

    //TODO
    @Override
    public double getUserBalance(long userId) {
        return 0;
    }

    //TODO
    @Override
    public String getLocale(long userId) {
        return null;
    }

    private long getAuthenticatedUserId(){
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
