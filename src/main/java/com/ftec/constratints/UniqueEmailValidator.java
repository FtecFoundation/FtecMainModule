package com.ftec.constratints;

import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserDAO userDAO;

    public UniqueEmailValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //TODO implement check
        User u = userDAO.findById(1L).get();
        System.out.println(u);
        return false;
    }

}
