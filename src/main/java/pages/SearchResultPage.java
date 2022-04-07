package pages;

import entities.Product;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static java.lang.String.format;

/**
 * @author Sergey Nesterov
 */
public class SearchResultPage extends BasePage {

    @FindBy(xpath = "//*[@data-widget='fulltextResultsHeader']")
    private WebElement resultHeader;

    @FindBy(xpath = "//aside/*[contains(@class,'filter-block')]")
    private List<WebElement> filterBlocks;
    private By switchBoxStatus = By.xpath(".//input"),
            switchBoxClick = By.xpath(".//div[1]"),
            seeAll = By.xpath(".//*[contains(text(),'Посмотреть все')]"),
            to = By.xpath(".//*[contains(text(),'до')]/../input"),
            from = By.xpath(".//*[contains(text(),'от')]/../input"),
            blockSearch = By.xpath(".//input[@type='text']"),
            firstInBlockList = By.xpath(".//a");

    private String checkBoxStatus = ".//*[contains(text(),'%s')]/../../input",
            checkBoxClick = ".//*[contains(text(),'%s')]/../../div[1]";

    @FindBy(xpath = "//*[contains(@class,'widget-search-result-container')]")
    private WebElement container;

    @FindBy(xpath = "//*[@data-widget='searchResultsFilters']")
    private WebElement filter;

    @FindBy(xpath = "//*[contains(text(),'Дальше')]")
    private WebElement next;

    @FindBy(xpath = "//*[contains(@class,'search-result')][1]/div/div")
    private List<WebElement> products;
    private By name = By.xpath(".//*[contains(@class,'tsBodyL')]"),
            cost = By.xpath("./div[3]//*[contains(text(),'₽') and not(contains(text(),'−'))][1]"),
            toBasket = By.xpath(".//*[contains(text(),'доставит')]/../..//*[contains(text(),'В корзину')]");

    public SearchResultPage isResultHeaderContains(String text) {
        Assertions.assertTrue(resultHeader.getText().contains(text), "Неправильно выполнен поиск '" + text + '"');
        return this;
    }

    /**
     * Установить чекбокс в блоке
     *
     * @param block название блока
     * @param name  название чекбокса
     * @return страницу поиска
     */
    public SearchResultPage setCheckBox(String block, String name) {
        waitVisio(filter);
        if (getCheckBoxIfPresent(block, name)) return this;
        for (WebElement we : filterBlocks) {
            if (!we.getText().contains(block)) continue;
            if (!isPresentThenClick(we, seeAll)) {
                we.findElement(By.xpath(format(checkBoxClick, name))).click();
            } else {                  //если блок соделжит раскрывающийся список
                WebElement searchInput = waitVisio(we, blockSearch);
                searchInput.click();
                searchInput.clear();
                searchInput.sendKeys(name);
                wait.until(ExpectedConditions.textToBePresentInElement(we.findElement(firstInBlockList), name));
                we.findElement(firstInBlockList).click();
                waitVisio(we, seeAll);
            }
            Assertions.assertTrue(getCheckBoxIfPresent(block, name), "Чекбокс '" + name + " в блоке " + block + "' не выбран");
            return this;
        }
        Assertions.fail("Отсутствует блок '" + block + "' в фильтре");
        return this;
    }

    /**
     * Проверить статус чекбокса, если он виден в блоке
     *
     * @param block название блока
     * @param name  название чекбокса
     * @return true - чекбокс установлен, false - чекбокс не виден или не установлен
     */
    public boolean getCheckBoxIfPresent(String block, String name) {
        waitVisio(filter);
        for (WebElement we : filterBlocks) {
            if (!we.getText().contains(block)) continue;
            try {
                return we.findElement(By.xpath(format(checkBoxStatus, name))).isSelected();
            } catch (NoSuchElementException ex) {
                return false;
            }
        }
        Assertions.fail("Отсутствует блок '" + block + "' в фильтре");
        return false;
    }


    public SearchResultPage setSwitchBox(String name) {
        waitVisio(filter);
        for (WebElement we : filterBlocks) {
            if (!we.getText().contains(name)) continue;
            if (!getSwitchBox(name)) we.findElement(switchBoxClick).click();
            {
                Assertions.assertTrue(getSwitchBox(name), "Cвитчбокс '" + name + "' не выбран");
                return this;
            }
        }
        Assertions.fail("Отсутствует свитчбокс '" + name + "'");
        return this;
    }

    public boolean getSwitchBox(String name) {
        waitVisio(filter);
        for (WebElement we : filterBlocks) {
            if (!we.getText().contains(name)) continue;
            return we.findElement(switchBoxStatus).isSelected();
        }
        Assertions.fail("Отсутствует свитчбокс '" + name + "'");
        return false;
    }

    public SearchResultPage setField(String name, String fromTo, String text) {
        waitVisio(filter);
        By by = fromTo.equals("от") ? from : to;
        for (WebElement we : filterBlocks) {
            if (!we.getText().contains(name)) continue;
            we.findElement(by).click();
            we.findElement(by).sendKeys(Keys.chord(Keys.CONTROL, "a"), text, Keys.ENTER);
            Assertions.assertEquals(text, getField(name, fromTo), "Неверно значение поля '" + name + "' " + fromTo);
            return this;
        }
        Assertions.fail("Отсутствует  поле '" + name + "' " + fromTo);
        return this;
    }

    public String getField(String name, String fromTo) {
        waitVisio(filter);
        By by = fromTo.equals("от") ? from : to;
        for (WebElement we : filterBlocks) {
            if (!we.getText().contains(name)) continue;
            return we.findElement(by).getAttribute("value").replaceAll("\\D", "");
        }
        Assertions.fail("Отсутствует поле '" + name + "' " + fromTo);
        return "";
    }


    public boolean addToBasket(WebElement we) {
        waitVisio(container);
        String countBefore = getBasketIconCount();
        if (!isPresentThenClick(we, toBasket)) return false;
        waitUntilBasketIconCountChange(countBefore);
        Product.addProduct(
                we.findElement(name).getText(),
                Integer.parseInt(we.findElement(cost).getText().replaceAll("\\D", "")));
        return true;
    }

    /**
     * Добавить в корзину товары из списка выдачи
     *
     * @param quantity ограничение по количеству добавляемых товаров, -1 - без ограничения
     * @param even     добавлять четные в списке
     * @param odd      добавлять нечетные в списке
     * @return экземпляр этой страницы
     */
    public SearchResultPage addProductsToBasket(int quantity, boolean even, boolean odd) {
        do {
            waitVisio(container);
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
