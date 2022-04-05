package steps;

import io.cucumber.java.ru.И;
import managers.PagesManager;
import pages.SearchResultPage;

import java.util.List;

/**
 * @author Sergey Nesterov
 */

public class SearchResultPageStep {

    private SearchResultPage searchResultPage = PagesManager.getInstance().getSearchResultPage();

    @И("^Установить '([^\"]+)' (до|от) (\\d+)$")
    public void setFieldTo(String name, String fromTo, int number) {
        searchResultPage.setField(name, fromTo, String.valueOf(number));
    }

    @И("^Установить свитчбокс '([^\"]+)'$")
    public void setSwitchBox(String name) {
        searchResultPage.setSwitchBox(name);
    }

    @И("^Установить в блоке '([^\"]+)' чекбокс '([^\"]+)'$")
    public void setCheckBox(String block, String name) {
        searchResultPage.setCheckBox(block, name);
    }

    @И("^Выбрать брэнды$")
    public void setBrandList(List<String> brands) {
        brands.forEach(b->setCheckBox("Бренды", b));
    }

    @И("^Добавить в корзину (четные|нечетные|первые) (\\d+) результатов$")
    public void addProductsToBasket(String param, int quantity) {
        if (param.equals("четные")) {
            searchResultPage.addProductsToBasket(quantity, true, false);
        } else if (param.equals("нечетные")) {
            searchResultPage.addProductsToBasket(quantity, false, true);
        } else if (param.equals("первые")) {
            searchResultPage.addProductsToBasket(quantity, true, true);
        }
    }

    @И("^Добавить в корзину все (четные|нечетные|все) результаты$")
    public void addProductsToBasket(String param) {
        addProductsToBasket(param.replace("все", "первые"), -1);
    }

    @И("^Перейти в корзину$")
    public void clickBasketIconCount() {
        searchResultPage.clickBasketIconCount();
    }
}
