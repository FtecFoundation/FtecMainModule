package com.ftec.constratints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ftec.repositories.UserDAO;

@Service
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserDAO userDAO;

    @Autowired
    public UniqueEmailValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    	if(email == null) return true;
    	
        return !userDAO.findByEmail(email).isPresent();
    }

}
