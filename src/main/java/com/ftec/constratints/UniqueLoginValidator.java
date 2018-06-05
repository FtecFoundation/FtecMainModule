package com.ftec.constratints;

import com.ftec.repositories.UserDAO;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueLoginValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserDAO userDAO;

    public UniqueLoginValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {

        //TODO implement
        return true;
    }

}
