package com.ftec.configs;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class UserRegTest {
	
	@Autowired
	UserDAO dao;

	@Autowired
	MockMvc mvc;
	
	@Test
	public void createValidUser() throws Exception {
		User u = new User();
		u.setUsername("user1");
		u.setPassword("pass_user1");
		u.setEmail("emaill");
		
		mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/registration/registr_test").
				content( asJsonString(u)).contentType(MediaType.APPLICATION_JSON).
				accept(MediaType.APPLICATION_JSON))

		.andDo(print()).andExpect(status().isCreated());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	public static final String regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(regexp);

	public static boolean validate(String emailStr) {
		        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		        return matcher.find();
	}
	
	@Test
	public void emailTestt() {
		String validEmails[] = new String[] {
                 "alex@yandex.ru",
                 "alex-27@yandex.com",
                 "alex.27@yandex.com",
                 "alex111@test.com",
                 "alex.100@test.com.ua",
                 "alex@1.com",
                 "alex@gmail.com.com",
                 "alex+27@gmail.com",
                 "alex-27@yandex-test.com"
         };
		
		 for (String email : validEmails) {
			assertTrue(validate(email));
		 }
		 
		 String unvalidEmails[] = new String[] {
                 "devcolibri",
                 "alex@.com.ua",
                 "alex123@gmail.a",
                 "alex123@.com",
                 "alex123@.com.com",
                 ".alex@devcolibri.com",
                 "alex()*@gmail.com",
                 "alex@%*.com",
                 "alex..2013@gmail.com",
                 "alex.@gmail.com",
                 "alex@devcolibri@gmail.com",
                 "alex@gmail.com.1ua"
         };
		 for (String email : unvalidEmails) {
				assertFalse(validate(email));
			}
	}
}
