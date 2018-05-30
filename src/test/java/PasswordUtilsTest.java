import static org.junit.Assert.assertEquals;

import com.ftec.utils.PasswordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

public class PasswordUtilsTest {

    @Test
    public void isPasswordMatchTest() {
        String rawPassword = "myPassword123";
        String securePassword = "HhaNvzTsVYwS/x/zbYXlLOE3ETMXQgllqrDaJY9PD/U=";
        String salt = "EqdmPh53c9x33EygXpTpcoJvc4VXLK";

        assertEquals(securePassword, PasswordUtils.generateSecurePassword(rawPassword, salt));
    }

    @Test
    public void encodeEmptyPasswordTest() {
        String rawPassword = "";
        String securedPassword = "x2ZjOrX31oOvmEx6HZT86la0/+qKgtLP9ZWHeVmDEn4=";
        String salt = "GUsnPZ8Y5AZX3lS07uHh94ocnnfvKP";

        //todo What expected?
        assertEquals(securedPassword, PasswordUtils.generateSecurePassword(rawPassword, salt));
    }
}