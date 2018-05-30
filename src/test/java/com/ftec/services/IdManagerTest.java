package com.ftec.services;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ids;
import com.ftec.entities.TelegramSettings;
import com.ftec.entities.User;
import com.ftec.entities.UserToken;
import com.ftec.services.interfaces.IdManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Profile("test")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class IdManagerTest {
    @Autowired
    public IdManager idManager;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        elasticsearchTemplate.deleteIndex(Ids.class);
        elasticsearchTemplate.createIndex(Ids.class);
    }

    @Test
    public void idManagerGetLastTest() {
        assert idManager.getLastId(UserToken.class)==0;
    }

    @Test
    public void idManagerGetLastExistingTest() {
        assert idManager.getLastId(TelegramSettings.class)>0;
    }

    @Test
    public void idManagerIncrementTest(){

        idManager.incrementLastId(User.class);
    }

}
