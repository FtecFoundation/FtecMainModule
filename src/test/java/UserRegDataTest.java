import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserRegDataTest {
	public static final String regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile(regexp);

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
