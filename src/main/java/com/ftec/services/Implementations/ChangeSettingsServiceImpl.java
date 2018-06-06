package com.ftec.services.Implementations;

import com.ftec.controllers.ChangeSettingController;
import com.ftec.entities.User;
import com.ftec.exceptions.UserNotExistsException;
import com.ftec.repositories.UserDAO;
import com.ftec.services.interfaces.ChangeSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChangeSettingsServiceImpl implements ChangeSettingsService {
    private final UserDAO userDAO;

    public ChangeSettingsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    @Override
    public void updatePreferences(ChangeSettingController.UserUpdate userUpdate, long userId) throws UserNotExistsException {
        Optional<User> userFromDB = userDAO.findById(userId);

        if(!userFromDB.isPresent()) {
            throw new UserNotExistsException();
        }
        User u = userFromDB.get();
        u.apllyChangeSettings(userUpdate);
        userDAO.save(u);
    }
}
