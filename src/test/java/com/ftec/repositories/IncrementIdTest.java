package com.ftec.repositories;


import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ids;
import com.ftec.entities.User;
import com.ftec.repositories.IdsDAO;
import com.ftec.repositories.UserDAO;
import com.ftec.services.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)

public class IncrementIdTest {

    @Autowired
    UserDAO userDAO;

    @Autowired
    IdsDAO idsDAO;

    private void printUser() {
        Iterable<User> allIteration = userDAO.findAll();
        Iterator<User> iterator = allIteration.iterator();
        for (User user : allIteration) {
            System.out.println(user);
        }
    }

    private void printIds() {
        Iterable<Ids> allIteration = idsDAO.findAll();
        Iterator<Ids> iterator = allIteration.iterator();
        for (Ids user : allIteration) {
            System.out.println(user);
        }
    }

    @Test
    public void tryIncrement() {
        printIds();
        Long cur_id = idsDAO.findByTableName(UserServiceImpl.com_ftec_entities_User).getLastId();
        idsDAO.incrementLastId("com.ftec.entities.User");
        assertTrue(idsDAO.findByTableName(UserServiceImpl.com_ftec_entities_User).getLastId() == 1 + cur_id);
    }
}
