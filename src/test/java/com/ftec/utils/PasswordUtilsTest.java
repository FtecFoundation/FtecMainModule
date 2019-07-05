package com.ftec.utils;

import com.ftec.utils.encoding.PasswordUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
public class PasswordUtilsTest {
    @Test
    public void isPasswordMatchTest() {
        String rawPassword = "myPassword123";
        String salt = "EqdmPh53c9x33EygXpTpcoJvc4VXLK";
        String securePassword = PasswordUtils.generateSecurePassword(rawPassword, salt);

        assert PasswordUtils.isPasswordMatch(rawPassword, securePassword, salt);
    }

    @Test
    public void encodeEmptyPasswordTest() {
        String rawPassword = "";
        String securedPassword = "x2ZjOrX31oOvmEx6HZT86la0/+qKgtLP9ZWHeVmDEn4=";
        String salt = "GUsnPZ8Y5AZX3lS07uHh94ocnnfvKP";

        assertEquals(securedPassword, PasswordUtils.generateSecurePassword(rawPassword, salt));
    }


}
