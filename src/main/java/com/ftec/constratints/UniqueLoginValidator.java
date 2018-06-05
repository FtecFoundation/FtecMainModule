package com.ftec.constratints;

import com.ftec.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {
    private final UserDAO userDAO;

    @Autowired
    public UniqueLoginValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {

        return !userDAO.findByUsername(username).isPresent();
    }

}
