import com.ftec.configs.ApplicationConfig;
import com.ftec.controllers.RegistrationController;
import com.ftec.services.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class RegistrationTest {
    @Autowired
    public RegistrationService registrationService;

    @Test
    public void testRegistration() {
        RegistrationController.RegistrationUser preRegisteredUser =
                new RegistrationController.RegistrationUser("testUsername","password","password",0, true);
        registrationService.registerUser(preRegisteredUser);
    }
}
