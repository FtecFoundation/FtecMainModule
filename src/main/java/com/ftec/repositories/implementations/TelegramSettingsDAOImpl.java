package com.ftec.repositories.implementations;

import com.ftec.entities.TelegramSettings;
import com.ftec.repositories.interfaces.TelegramSettingsDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class TelegramSettingsDAOImpl implements TelegramSettingsDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(TelegramSettings settings) {
        entityManager.persist(settings);
    }

    @Override
    public void update(TelegramSettings settings) {
        entityManager.merge(settings);
    }

    @Override
    public void updateNotifications(long userId, String notifications) {
        entityManager.createQuery("update TelegramSettings set enabledNotifications = :enabled where userId=:user")
                .setParameter("enabled", notifications)
                .setParameter("user", userId)
                .executeUpdate();
    }

    @Override
    public TelegramSettings get(long id) {
        try {
            return entityManager.createQuery("select ts from TelegramSettings ts where ts.userId=:user", TelegramSettings.class)
                    .setParameter("user", id)
                    .getSingleResult();
        }catch (NoResultException ex){
            TelegramSettings ts = new TelegramSettings();
            ts.setEnabled(false);
            ts.setUserId(id);
            ts.setEnabledNotifications("");
            ts.setLinkedUserChatId(0);
            ts.setLinkedUsername("");
            persist(ts);
            return ts;
        }
    }

    @Override
    public void deleteCode(long userId) {
        entityManager.createQuery("update TelegramSettings set accessCode=null, enabled=false, linkedUserChatId=0, linkedUsername='' where userId=:user")
                .setParameter("user", userId)
                .executeUpdate();
    }

    @Override
    public void saveCode(long userId, String newCode) {
        entityManager.createQuery("update TelegramSettings set accessCode=:code, enabled=true where userId=:user")
            .setParameter("code",newCode)
            .setParameter("user", userId)
            .executeUpdate();
    }
}
