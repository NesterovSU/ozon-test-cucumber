import entities.Product;
import managers.DriverManager;
import managers.PagesManager;
import managers.PropertiesManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.MyProp;
import utils.MyScreenshot;

/**
 * @author Sergey Nesterov
 */
@ExtendWith(MyScreenshot.class)
public class BaseTestFor {
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
        Product.clearList();
    }
}
