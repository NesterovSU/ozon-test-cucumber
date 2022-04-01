package steps;

import io.cucumber.java.ru.И;
import managers.PagesManager;
import pages.HomePage;

/**
 * @author Sergey Nesterov
 */
public class HomePageStep {

    private HomePage homePage = PagesManager.getInstance().getHomePage();

    @И("^Ввести в поиск '([^\"]+)'$")
    public void search(String request){
        homePage.search(request);
    }
}
