package pages;

import managers.DriverManager;
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
        driver = DriverManager.getInstance();
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 10, 100);
//        wait.ignoring(org.openqa.selenium.StaleElementReferenceException.class);
    }


    public SearchResultPage search(String text) {
        searchField.click();
        searchField.clear();
        searchField.sendKeys(text);
        searchButton.click();
        return PagesManager.getInstance().getSearchResultPage();
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
        int timeout = 5; //*0.1 sec
        do {
            try {
                return wait.until(ExpectedConditions.visibilityOf(we));
            } catch (org.openqa.selenium.StaleElementReferenceException ex) { }
            mySleep(100);
        } while (--timeout > 0);
        return we;
    }

    public boolean isPresentThenClick(WebElement we) {
        By by = By.xpath(".");
        return isPresentThenClick(we, by);
    }

    public boolean isPresentThenClick(WebElement we, By by) {
        try {
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
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
