package com.ftec.constratints;

import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftec.configs.ApplicationConfig;

@ActiveProfiles("test")
@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class UniqueEmailConstraintTest {

    @Autowired
    private Validator validator;

    @Test
    public void checkConstraint() {
//        ChangeSettingController.UserUpdate user = new ChangeSettingController.UserUpdate();
//        user.setPassword("123456");
//        user.setUserId(1);
//        user.setTwoFactorEnabled(false);
//        user.setEmail("saf");
//        Set<ConstraintViolation<ChangeSettingController.UserUpdate>> violationSet = validator.validate(user);
//        assert violationSet.size()==1;
    }
}