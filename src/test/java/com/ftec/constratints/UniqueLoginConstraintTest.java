package com.ftec.constratints;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.utils.EntityGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@ActiveProfiles(value = "test", inheritProfiles = false)
@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringRunner.class)
public class UniqueLoginConstraintTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UniqueLoginValidator validator;

    @Test
    public void test(){
        String username = "UniqueLoginTest";

        assertTrue(validator.isValid(username,null));
        User user1 = EntityGenerator.getNewUser();
        user1.setUsername(username);
        userDAO.save(user1);

        assertFalse(validator.isValid(username,null));

    }
    @Test
    public void testNullLogin(){
        assertTrue(validator.isValid(null,null));

    }
}