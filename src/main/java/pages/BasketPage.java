package pages;

import entities.Product;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Nesterov
 */
public class BasketPage extends BasePage {

    @FindBy(xpath = "//*[@data-widget='split']/div")
    private List<WebElement> products;

    private By name = By.xpath("./div[2]/a/span"),
            cost = By.xpath("./div[3]/div[1]"),
            delete = By.xpath(".//span[contains(text(),'Удалить')]");

    @FindBy(xpath = "//*[contains(text(),'Выбрать все')]//input")
    private WebElement chooseAll;

    @FindBy(xpath = "//span[contains(text(),'Удалить выбранные')]")
    private WebElement deleteChecked;

    @FindBy(xpath = "//*[contains(text(),'Вы точно хотите удалить выбранные товары')]/" +
            "../div[3]//button")
    private WebElement deleteAccept;

    @FindBy(xpath = "//h1[contains(text(),'Корзина пуста')]")
    private WebElement basketEmpty;

    @FindBy(xpath = "//*[@data-widget='total']")
    private WebElement total;

    @FindBy(xpath = "//*[contains(@text,'Перейти к оформлению') and @size='xxl']")
    private WebElement registrationButton;

    @FindBy(xpath = "//span[text()='Добавить компанию']/../../../../../div[3]//button")
    private WebElement advertising;


    public BasketPage checkTotalContains(String text1, String text2) {
        String str = waitVisio(total).getText();
        Assertions.assertTrue(
                str.contains(text1) &&
                        str.contains(text2),
                "Отсутствует надпись '" + text1 + " " + text2 + "(ов)'");
        return this;
    }

    public boolean isProductInBasket(String name, int cost) {
        waitVisio(registrationButton);
        for (WebElement item : products) {
            if (item.getText().contains("Доставка Ozon")) continue;
            if (item.findElement(this.name).getText().contains(name) &&
                    item.findElement(this.cost).getText().replaceAll("\\D", "")
                            .contains(String.valueOf(cost))) {
                scrollTo(item);
                return true;
            }
        }
        return false;
    }

    public BasketPage deleteAllProducts() {
        String countBefore = getBasketIconCount();
        waitVisio(deleteChecked).click();
        waitVisio(deleteAccept).click();
        waitUntilBasketIconCountChange(countBefore);
        return this;
    }

    public BasketPage closeAdvertising() {
        try {
            advertising.click();
        } catch (NoSuchElementException ex) {
        }
        return this;
    }

    public boolean isBasketEmpty() {
        try {
            wait.until(ExpectedConditions.visibilityOf(basketEmpty));
            return true;
        } catch (TimeoutException ex) {
            Assertions.fail("Отсутствует заголовок 'Корзина пуста'");
            return false;
        }
    }

    public BasketPage checkProductsInBasket(List<Product> productList) {
        List<Product> basketList = getProductList();
        productList.forEach(p -> Assertions.assertTrue(basketList.contains(p),
                "В корзине отсутствует " + p));
        return this;
    }

    public List<Product> getProductList() {
        waitVisio(registrationButton);
        List<Product> basketList = new ArrayList<>();
        for (WebElement item : products) {
            if (item.getText().contains("Доставка")) continue;
            scrollTo(item);
            basketList.add(new Product(
                    item.findElement(name).getText(),
                    Integer.parseInt(item.findElement(cost).getText().replaceAll("\\D", ""))));
        }
        return basketList;
    }
}
