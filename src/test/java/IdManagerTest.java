import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.services.interfaces.IdManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationConfig.class)
public class IdManagerTest {
    @Autowired
    public IdManager idManager;

    @Test
    public void idManagerGetLastTest() {
        assert idManager.getLastId(User.class)==1;
    }

    @Test
    public void idManagerGetLastExistingTest() {
        System.out.println(idManager.getLastId(User.class));
    }
}
