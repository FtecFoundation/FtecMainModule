import static org.junit.Assert.assertEquals;

import com.ftec.utils.PasswordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

public class PasswordEncodeTest {

    @Test
    public void isPasswordMatch() {
        String rawPassword = "myPassword123";
        String securePassword = "HhaNvzTsVYwS/x/zbYXlLOE3ETMXQgllqrDaJY9PD/U=";
        String salt = "EqdmPh53c9x33EygXpTpcoJvc4VXLK";

        assertEquals(securePassword, PasswordUtils.generateSecurePassword(rawPassword, salt));


    }
}