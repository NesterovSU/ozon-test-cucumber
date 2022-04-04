package pages;

import entities.Product;
import managers.PagesManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * @author Sergey Nesterov
 */
public class SearchResultPage extends BasePage {

    @FindAll({
            @FindBy(xpath = "//aside//*[contains(@value,'Высокий рейтинг')]"),
    })
    private List<WebElement> switchBoxList;
    private By switchBoxStatus = By.xpath(".//input"),
            switchBoxClick = By.xpath(".//div[1]");

    @FindAll({
            @FindBy(xpath = "//aside//*[contains(text(),'NFC')]")
    })
    private List<WebElement> chekBoxList;
    private By checkBoxStatus = By.xpath("./../../input"),
            checkBoxClick = By.xpath("./../../div");

    @FindBy(xpath = "//aside//*[contains(text(),'Все фильтры')]")
    private WebElement allFilters;

    @FindAll({
            @FindBy(xpath = "//aside//*[contains(text(),'Цена')]/..")
    })
    private List<WebElement> fields;
    private By to = By.xpath(".//*[contains(text(),'до')]/../input"),
            from = By.xpath(".//*[contains(text(),'от')]/../input");

    @FindBy(xpath = "//aside")
    private WebElement filter;

    private final String BRAND = "//*[@data-widget='searchResultsFilters']//*[contains(text(),'Бренды')]/..";
    @FindBy(xpath = BRAND + "//*[contains(text(),'Посмотреть все')]")
    private WebElement brandsOpen;
    @FindBy(xpath = BRAND + "//input[@type='text']")
    private WebElement brandSearch;
    @FindBy(xpath = BRAND + "//a")
    private List<WebElement> brandList;
    private By checkBox = By.xpath(".//input");
    @FindBy(xpath = "//*[contains(text(),'Дальше')]")
    private WebElement next;

    @FindBy(xpath = "//*[contains(@class,'search-result')][1]/div/div/div/..")
    private List<WebElement> products;

    private By name = By.xpath(".//*[contains(@class,'tsBodyL')]"),
            cost = By.xpath("./div[3]//*[contains(text(),'₽') and not(contains(text(),'−'))][1]"),
            toBasket = By.xpath(".//*[contains(text(),'доставит')]/../..//*[contains(text(),'В корзину')]"),
            containerResults = By.xpath("//*[contains(@class,'search-result')]");


    public SearchResultPage setBrand(String brand) {
        waitVisio(brandsOpen).click();
        waitVisio(brandSearch).click();
        brandSearch.clear();
        brandSearch.sendKeys(brand);
        wait.until(ExpectedConditions.textToBePresentInElement(brandList.get(0), brand));
        brandList.get(0).click();
        Assertions.assertTrue(isBrandSelected(brand), "Не выбран брэнд в фильтре - " + brand);
        return this;
    }

    public SearchResultPage setBrandList(List<String> brands) {
        brands.forEach(b -> PagesManager.getInstance().getSearchResultPage().setBrand(b));
        return this;
    }


    public boolean isBrandSelected(String brand) {
        waitVisio(brandsOpen);
        for (WebElement item : brandList) {
            if (item.getText().contains(brand))
                return item.findElement(checkBox).isSelected();
        }
        return false;
    }

    public SearchResultPage setCheckBox(String name) {
        waitVisio(allFilters);
        for (WebElement we : chekBoxList) {
            if (!we.getText().contains(name)) continue;
            if (!getCheckBox(name)) waitVisio(we, checkBoxClick).click();
            {
                Assertions.assertTrue(getCheckBox(name), "Чекбокс '" + name + "' не выбран");
                return this;
            }
        }
        Assertions.fail("Отсутствует чекбокс '" + name + "'");
        return this;
    }

    public boolean getCheckBox(String name) {
        waitVisio(allFilters);
        for (WebElement we : chekBoxList) {
            return waitVisio(we, checkBoxStatus).isSelected();
        }
        Assertions.fail("Отсутствует чекбокс '" + name + "'");
        return false;
    }


    public SearchResultPage setSwitchBox(String name) {
        waitVisio(allFilters);
        for (WebElement we : switchBoxList) {
            if (!we.getText().contains(name)) continue;
            if (!getSwitchBox(name)) waitVisio(we, switchBoxClick).click();
            {
                Assertions.assertTrue(getSwitchBox(name), "Cвитчбокс '" + name + "' не выбран");
                return this;
            }
        }
        Assertions.fail("Отсутствует свитчбокс '" + name + "'");
        return this;
    }

    public boolean getSwitchBox(String name) {
        waitVisio(allFilters);
        for (WebElement we : switchBoxList) {
            return waitVisio(we, switchBoxStatus).isSelected();
        }
        Assertions.fail("Отсутствует свитчбокс '" + name + "'");
        return false;
    }


    public SearchResultPage setFieldTo(String name, String text) {
        waitVisio(allFilters);
        for (WebElement we : fields) {
            if (!we.getText().contains(name)) continue;
            waitVisio(we, to).click();
            we.findElement(to).sendKeys(Keys.chord(Keys.CONTROL, "a"), text, Keys.ENTER);
            Assertions.assertEquals(text, getFieldTo(name), "Неверно значение поля '" + name + "' от");
            return this;
        }
        Assertions.fail("Отсутствует  поле '" + name + "' от");
        return this;
    }

    public String getFieldTo(String name) {
        waitVisio(allFilters);
        for (WebElement we : fields) {
            if (!we.getText().contains(name)) continue;
            return waitVisio(we, to).getAttribute("value").replaceAll("\\D", "");
        }
        Assertions.fail("Отсутствует поле '" + name + "' от");
        return "";
    }


    public boolean addToBasket(WebElement we) {
        waitVisio(we);
        String countBefore = getBasketIconCount();
        if (!isPresentThenClick(we, toBasket)) return false;
        waitUntilBasketIconCountChange(countBefore);
        Product.addProduct(
                we.findElement(name).getText(),
                Integer.parseInt(we.findElement(cost).getText().replaceAll("\\D", "")));
        return true;
    }

    public SearchResultPage addAllProductsToBasket(boolean even, boolean odd) {
        return addProductsToBasket(-1, even, odd);
    }

    /**
     * @param quantity ограничение по количеству добавляемых продуктов, -1 - без ограничения
     * @param even     добавлять четные продукты
     * @param odd      добавлять нечетные продукты
     * @return экземпляр этой страницы
     */
    public SearchResultPage addProductsToBasket(int quantity, boolean even, boolean odd) {
        do {
            waitVisio(driver.findElement(containerResults));
            for (int i = 0; i < products.size(); i++) {
                if ((i + 1) % 2 != 0 && !odd) continue; //нечетное пропускаем
                if ((i + 1) % 2 == 0 && !even) continue; //четное пропускаем
                scrollTo(products.get(i));
                if (addToBasket(products.get(i)))
                    if (--quantity == 0) return this;
            }
        } while (isPresentThenClick(next));
        return this;
    }
}
