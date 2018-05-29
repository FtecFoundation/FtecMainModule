import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.UserTokenDAO;
import com.ftec.services.TokenService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class TokenSecurityTest {
	
	@Autowired
	UserDAO userDao;

	@Autowired
	MockMvc mvc;
	
	@Autowired
	UserTokenDAO tokenDao;
	
	@Autowired
	TokenService tokenService;
	
	public static User newUser(String login) {
		User u = new User();
		u.setUsername(login);
		u.setPassword("pass_user1");
		u.setEmail("emaildl");
		return u;
	}
	//failed test, getByToken from UserTokenDAO can't convert json-data with Date field into UserToken
	@Test
	public void securityAccess() throws Exception {
		String token = tokenService.createAndGetToken(255L);
		System.out.println("generate token = " + token);
		
		mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/securedTest")
				.header(TokenService.TOKEN_NAME, token)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isAccepted());
		
		tokenDao.deleteById(token);
	}
	
	@Test
	public void tryAccessWithoutValidToken() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/securedTest")
				.header(TokenService.TOKEN_NAME, "123_UNVALIDTOKEN")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isBadRequest());
	}
	
}
