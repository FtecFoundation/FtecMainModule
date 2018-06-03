package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ftec.configs.ApplicationConfig;
import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.TokenService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class DBtest {

	@Autowired
	MockMvc mvc;
	
	@Autowired
	UserTokenDAO tokenDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TokenService service;
	
	@Before
	public void setUp() {
		userDAO.deleteAll();
		tokenDAO.deleteAll();
	}


	@Test
	public void userDBtest() {
		assertFalse(userDAO.findAll().iterator().hasNext()); //isEmptyTable
		Long id = 11L;
		
		String username = "user1";
		String password = "pass1";
		String email = "email@d.net";
		TutorialSteps currentStep = TutorialSteps.FIRST;
		boolean subscribeForNews = false;
		Boolean twoStepVerification = false;
		
		User u = new User(id, username, password,
				email, currentStep, subscribeForNews, twoStepVerification);
		userDAO.save(u);
		//works with 1 user
		assertTrue(userDAO.findById(id).get().getUsername().equals(username));
		assertTrue(userDAO.findByUsername(username).get().getPassword().equals(password));
		assertTrue(userDAO.findByEmail(email).get().getUsername().equals(username));
		assertTrue(userDAO.findByCurrentStep(currentStep).get().getUsername().equals(username));
		
		userDAO.deleteById(id);
		assertFalse(userDAO.findById(id).isPresent());

		userDAO.save(u);
		assertTrue(userDAO.findById(id).get().getUsername().equals(username));
		userDAO.deleteByEmail(email);
		assertFalse(userDAO.findById(id).isPresent());
			
		userDAO.save(u);
		assertTrue(userDAO.findByCurrentStep(currentStep).get().getUsername().equals(username));
		userDAO.deleteByCurrentStep(currentStep);
		assertFalse(userDAO.findById(id).isPresent());
		
		u.setEmail("new_email");
		userDAO.save(u);
		
		assertTrue(userDAO.findByEmail("new_email").get().getUsername().equals(username));
		userDAO.deleteById(id);
		
		assertFalse(userDAO.findById(id).isPresent());
	}
	
	
	@Test
	public void TokenDBtest() {
		String token = service.createSaveAndGetNewToken(132L);
		//tokenDAO.findById(id)
	}

}
