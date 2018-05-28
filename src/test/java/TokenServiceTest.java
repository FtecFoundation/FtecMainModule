import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ftec.services.TokenService;

@RunWith(SpringRunner.class)
public class TokenServiceTest {
	
	TokenService service = new TokenService();
	
	@Test
	public void test() {
		assertThat(service.extractIdFromToken("23_NDKJAWNWKAJDNA WKDNAW"),is(23L));
	}
}
