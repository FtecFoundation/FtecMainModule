package com.ftec.services.Implementations;

import com.ftec.controllers.RegistrationController;
import com.ftec.entities.User;
import com.ftec.services.interfaces.IdManager;
import com.ftec.services.interfaces.RegistrationService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final IdManager idManager;

    public RegistrationServiceImpl(IdManager idManager) {
        this.idManager = idManager;
    }

    @Override
    public User registerUser(RegistrationController.UserRegistration userRegistration) {
        User userToSave = new User(userRegistration);
        userToSave.setId(idManager.getLastId(User.class));
        //todo Put increment method into getLastId
        idManager.incrementLastId(User.class);
        userToSave.fillEmptyFields();
        return userToSave;
    }
}
