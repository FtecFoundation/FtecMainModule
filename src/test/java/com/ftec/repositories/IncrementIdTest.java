package com.ftec.repositories;


import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
<<<<<<< HEAD
import org.springframework.context.annotation.Profile;
=======
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ActiveProfiles;
>>>>>>> a7c08944fb9d0ec724a6a88b56e31e118e7bf294
import org.springframework.test.context.junit4.SpringRunner;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.Ids;
import com.ftec.entities.User;
import com.ftec.services.interfaces.IdManager;
<<<<<<< HEAD

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
public class IncrementIdTest {

	@Autowired
	UserDAO userDAO;
	
	@Autowired 
	IdsDAO idsDAO;
	
	@Autowired
	IdManager idManager;
	
	 @Autowired
	 ElasticsearchTemplate elasticsearchTemplate;
	 
	 @Autowired
	 ApplicationContext applicationContext;
	 
	 @Before
	 public void setUp() throws Exception {
	    elasticsearchTemplate.deleteIndex(Ids.class);
	    elasticsearchTemplate.createIndex(Ids.class);
	    
	 }
	 
	private void printUser() {
		Iterable<User> allIteration = userDAO.findAll();
		Iterator<User> iterator = allIteration.iterator();
		for (User user : allIteration) {
			System.out.println(user);
		}
	}
	
	private void printIds() {
		Iterable<Ids> allIteration = idManager.findAll();
		Iterator<Ids> iterator = allIteration.iterator();
		for (Ids user : allIteration) {
			System.out.println(user);
		}
	}
	
	@Test
	public void tryIncrement() {
		printIds();
		Long cur_id = idManager.findByTableName(User.class).getLastId();
		idManager.incrementLastId(User.class);
		assertTrue(idManager.findByTableName(User.class).getLastId() == 1 + cur_id);
		//do not forgot decrement id 1 time
	}
	
	@Test
	public void tryIncrementManyTimes() {
		printIds();
		Long cur_id = idManager.findByTableName(User.class).getLastId();
		idManager.incrementLastId(User.class);
		idManager.incrementLastId(User.class);
		idManager.incrementLastId(User.class);
		assertTrue(idManager.findByTableName(User.class).getLastId() == 3 + cur_id);
		//do not forgot decrement id 3 times
	}
	
	
}