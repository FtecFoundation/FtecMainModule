package com.ftec.services.interfaces;

import com.ftec.entities.User;
import com.ftec.exceptions.UserExistException;

public interface UserService {

    User getById(long id);

    User registerNewUserAccount(User user) throws UserExistException;

}
