package pages;

import entities.Product;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * @author Sergey Nesterov
 */
public class SearchResultPage extends BasePage {

//    private By highRatingStatus = By.xpath("//aside//*[contains(text(),'Высокий рейтинг')]/../../../input"),
//            highRatingClick = By.xpath("//aside//*[contains(text(),'Высокий рейтинг')]/../../../div"),
//            NFCStatus = By.xpath("//aside//*[contains(text(),'NFC')]/../../input"),
//            NFCClick = By.xpath("//aside//*[contains(text(),'NFC')]/../../div"),
//            costMax = By.xpath("//aside//*[contains(text(),'Цена')]/..//*[contains(text(),'до')]/../input"),
//            filter = By.xpath("//aside");
////             = By.xpath(""),


    @FindBy(xpath = "//aside//*[contains(text(),'Высокий рейтинг')]/../../../input")
    private WebElement highRatingStatus;
    @FindBy(xpath = "//aside//*[contains(text(),'Высокий рейтинг')]/../../../div")
    private WebElement highRatingClick;
    @FindBy(xpath = "//aside//*[contains(text(),'NFC')]/../../input")
    private WebElement NFCStatus;
    @FindBy(xpath = "//aside//*[contains(text(),'NFC')]/../../div")
    private WebElement NFCClick;
    @FindBy(xpath = "//aside//*[contains(text(),'Цена')]/..//*[contains(text(),'до')]/../input")
    private WebElement costMax;

    @FindBy(xpath = "//*[contains(@class,'search-result')]/*/div")
    private List<WebElement> products;

    private By name = By.xpath(".//*[contains(@class,'tsBodyL')]"),
            cost = By.xpath(".//*[contains(@class,'ui-n6')]"),
            toBasket = By.xpath(".//*[contains(text(),'доставит')]/../..//button"),
            container = By.xpath("//*[contains(@class,'search-result')]");

    @Step("Установить чекбокс 'Высокий рейтинг'")
    public SearchResultPage setHighRating() {
        if (!getHighRating()) waitVisio(highRatingClick).click();
        return this;
    }

    public boolean getHighRating() {
        return waitVisio(highRatingStatus).isSelected();
    }

    @Step("Установить чекбокс 'NFC'")
    public SearchResultPage setNFC() {
        if (!getNFC()) waitVisio(NFCClick).click();
        return this;
    }

    public boolean getNFC() {
        return waitVisio(NFCStatus).isSelected();
    }

    @Step("Установить максимальную стоимость {cost}")
    public SearchResultPage setCostMax(int cost) {
        WebElement we = waitVisio(costMax);
        we.click();
        we.sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(cost), Keys.ENTER);
        return this;
    }

    public int getCostMax() {
        return Integer.parseInt(waitVisio(costMax).getAttribute("value").replaceAll("\\D", ""));
    }

    public List<WebElement> getProducts() {
        waitVisio(driver.findElement(container));
        return products;
    }

    @Step("Добавить продукт в корзину")
    public boolean addToBasket(WebElement we) {
        waitVisio(we);
        String countBefore = getBasketIconCount();
        try {
            we.findElement(toBasket).click();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        waitUntilBasketIconCountChange(countBefore);
        Product.addProduct(
                we.findElement(name).getText(),
                Integer.parseInt(we.findElement(cost).getText().replaceAll("\\D", "")));
        return true;
    }
}
