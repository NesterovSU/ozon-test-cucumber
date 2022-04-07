package pages;

import managers.DriversManager;
import managers.PagesManager;
import managers.PropertiesManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.MyProp;

import java.util.concurrent.TimeUnit;


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
        driver = DriversManager.getInstance();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 15);
//        wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }


    public SearchResultPage search(String text) {
        searchField.click();
        searchField.clear();
        searchField.sendKeys(text);
        searchButton.click();
        return PagesManager.getInstance().getSearchResultPage().isResultHeaderContains(text);
    }

    public BasketPage clickBasketIconCount() {
        basketIconCount.click();
        return PagesManager.getInstance().getBasketPage();
    }

    public String getBasketIconCount() {
        return waitVisio(basketIconCount).getText();
    }

    public void waitUntilBasketIconCountChange(String countBefore) {
        wait.until(ExpectedConditions.not(
                ExpectedConditions.textToBePresentInElement(basketIconCount, countBefore))
        );
    }

    public void scrollTo(WebElement we) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView()", we);
    }

    public WebElement waitVisio(WebElement we) {
        By by = By.xpath(".");
        return waitVisio(we, by);
    }

    /**
     * Ждать отображение элемента на странице, игнорируя StaleElementReferenceException
     * @param we блок
     * @param by элемент
     * @return элемент
     */
    public WebElement waitVisio(WebElement we, By by) {
        int timeout = 10; //*0.1 sec
        do {
            try {
                return wait.until(ExpectedConditions.visibilityOf(we.findElement(by)));
            } catch (org.openqa.selenium.StaleElementReferenceException ex) {
                mySleep(100);
            }
        } while (--timeout > 0);
        Assertions.fail("Элемент не обновился");
        return we;
    }

    public boolean isPresentThenClick(WebElement we) {
        By by = By.xpath(".");
        return isPresentThenClick(we, by);
    }

    /**
     * Нажать на элемент, если он присутствует на странице, без ожидания
     * @param we блок
     * @param by элемент внутри блока
     * @return true - если элемент найден, false - если не найден
     */
    public boolean isPresentThenClick(WebElement we, By by) {
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            we.findElement(by).click();
            long implWait = Long.parseLong(PropertiesManager.getInstance().get(MyProp.IMPLWAIT));
            driver.manage().timeouts().implicitlyWait(implWait, TimeUnit.SECONDS);
            return true;
        } catch (NoSuchElementException ex) {
            long implWait = Long.parseLong(PropertiesManager.getInstance().get(MyProp.IMPLWAIT));
            driver.manage().timeouts().implicitlyWait(implWait, TimeUnit.SECONDS);
            return false;
        }
    }

    public void mySleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
