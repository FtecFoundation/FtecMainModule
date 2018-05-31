package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ids;
import com.ftec.entities.TelegramSettings;
import com.ftec.entities.User;
import com.ftec.entities.UserToken;
import com.ftec.repositories.IdsDAO;
import com.ftec.services.interfaces.IdManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class IdManagerTest {
    @Autowired
    IdManager idManager;
    @Autowired
    IdsDAO idsDAO;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void setUp() throws Exception {
        elasticsearchTemplate.deleteIndex(Ids.class);
        elasticsearchTemplate.createIndex(Ids.class);
    }

    @Test
    public void idManagerGetLastNotExistsTest() {
        assert idManager.getLastId(UserToken.class)==1;
    }

    @Test
    public void idManagerGetLastExistingTest() {
        idsDAO.save(new Ids(TelegramSettings.class.getName(), 1));
        assert idManager.getLastId(TelegramSettings.class)>0;
    }

    @Test
    public void idManagerIncrementTest(){
        idsDAO.save(new Ids(User.class.getName(), 1));
        idManager.incrementLastId(User.class);
        assert idsDAO.findByTableName(User.class.getName()).getLastId()==2;
    }

}
