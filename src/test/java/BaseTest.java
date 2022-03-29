import managers.DriverManager;
import managers.PagesManager;
import managers.PropertiesManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utils.MyProp;

/**
 * @author Sergey Nesterov
 */
public class BaseTest {
    protected static PropertiesManager properties = PropertiesManager.getInstance();

    @BeforeEach
    void beforeEach() {
        DriverManager.getInstance().get(properties.get(MyProp.HOME_URL));
    }

    @AfterEach
    void afterEach() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        DriverManager.quit();
        PagesManager.deleteInstance();
    }
}
