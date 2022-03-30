package pages;

import io.qameta.allure.Step;
import managers.DriverManager;
import managers.PagesManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * @author Sergey Nesterov
 */
public class BasePage {

    @FindBy(xpath = "//*[contains(@data-widget,'headerIcon')]/span")
    private WebElement basketIconCount;

    @FindBy(xpath = "//form[@action='/search']//input[@name='text']")
    private WebElement searchField;

    @FindBy(xpath = "//form[@action='/search']//button")
    private WebElement searchButton;

    protected WebDriver driver;
    protected WebDriverWait wait;

    BasePage() {
        driver = DriverManager.getInstance();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 5, 100);
//        wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }

    @Step("Ввести запрос {text} в поле поиска")
    public void search(String text) {
        searchField.click();
        searchField.clear();
        searchField.sendKeys(text);
        searchButton.click();
    }
    @Step("Кликнуть на иконку корзины")
    public BasketPage clickBasketIconCount() {
        waitVisio(basketIconCount).click();
        return PagesManager.getInstance().getBasketPage();
    }

    @Step("Вернуть количество из иконки корзины")
    public String getBasketIconCount() {
        return waitVisio(basketIconCount).getText();
    }

    @Step("Подождать изменения количества в иконке корзины")
    public void waitUntilBasketIconCountChange(String countBefore) {
        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBePresentInElement(basketIconCount, countBefore))
        );
    }

    public void scrollTo(WebElement we) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", we);
    }

    public WebElement waitVisio(By by) {
        return waitVisio(driver.findElement(by));
    }

    public WebElement waitVisio(WebElement we) {
        int timeout = 50; //*0.1 sec
        do {
            try {
                return wait.until(ExpectedConditions.visibilityOf(we));
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return we;
            }
            timeout--;
        } while (timeout > 0);
        return we;
    }
}
