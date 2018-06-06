package com.ftec.constratints;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.services.interfaces.RegistrationService;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class UniqueEmailConstraintTest {

    @Autowired
    UniqueEmailValidator validator;
    @Autowired
    RegistrationService registrationService;

    @Test
    public void checkConstraint() {
        User u = EntityGenerator.getNewUser();

        assert validator.isValid("email@email.com", null);
        registrationService.registerNewUserAccount(u);

        assert !validator.isValid(u.getEmail(), null);
    }

    @Test
    public void checkNullEmail() {
        assert validator.isValid(null, null);
    }
}