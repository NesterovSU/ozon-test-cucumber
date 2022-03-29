package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * @author Sergey Nesterov
 */
public class BasketPage extends BasePage {

    @FindBy(xpath = "//*[@class='at4']")
    private List<WebElement> products;

    private By name = By.xpath(".//a[@class='t5a']/span"),
            cost = By.xpath(".//div[@class='u1a']/span"),
            delete = By.xpath(".//span[contains(text(),'Удалить')]");

    @FindBy(xpath = "//span[contains(text(),'Удалить выбранные')]")
    private WebElement deleteChecked;
}
