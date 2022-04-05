package steps;

import entities.Product;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import managers.DriversManager;
import managers.PagesManager;
import managers.PropertiesManager;
import utils.MyProp;


/**
 * @author Sergey Nesterov
 */
public class Hook {

    @Before(value = "@myTag")
    public void openHomePage() {
        DriversManager.getInstance().get(
                PropertiesManager.getInstance().get(MyProp.HOME_URL));
    }

    @After(value = "@myTag")
    public void closeBrowserWindow() {
        DriversManager.quit();
        PagesManager.deleteInstance();
        Product.attachInfo();
        Product.clearList();
    }
}
