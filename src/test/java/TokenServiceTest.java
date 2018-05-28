import com.ftec.exceptions.InvalidTokenException;
import com.ftec.services.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class TokenServiceTest {
	
	TokenService service = new TokenService(null);
	
	@Test
	public void getValidIdFromTokenTest() {
		assertThat(TokenService.getUserIdFromToken("23_NDKJAWNWKAJDNAkWKDNAW"),is(23L));
	}
	
	@Test
	public void tokenFormatTest() {
		Long testId = 199L;
		String generatedToken = TokenService.generateToken(testId);
		
		assertTrue(generatedToken.startsWith("199_"));
		
		String userId = generatedToken.substring(0, generatedToken.indexOf("_"));
		
		assertThat(Long.valueOf(userId), is(testId));
	}
	
	@Test(expected = InvalidTokenException.class)
	public void firstTestExceptionWhileInvalidTokenFormat() {
		TokenService.checkTokenFormat("23a_NDKJAWNWKAJDNAkWKDNAW");
		
		
	}
	@Test(expected = InvalidTokenException.class)
	public void secondTestExceptionWhileInvalidTokenFormat() {
		TokenService.checkTokenFormat("23aNDKJAWNWKAJDNAkWKDNAW");
		
		
	}
}
