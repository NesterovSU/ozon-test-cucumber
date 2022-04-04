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

    @И("^Установить максимальную цену (\\d+)$")
    public void setCostMax(int costMax) {
        searchResultPage.setCostMax(costMax);
    }

    @И("^Установить свитчбокс '([^\"]+)'$")
    public void setSwitchBox(String name) {
        searchResultPage.setSwitchBox(name);
    }

    @И("^Установить чекбокс '([^\"]+)'$")
    public void setCheckBox(String name) {
        searchResultPage.setCheckBox(name);
    }

    @И("^Выбрать брэнды$")
    public void setBrandList(List<String> brands) {
        searchResultPage.setBrandList(brands);
    }

    @И("^Добавить в корзину (четные|нечетные|первые) (\\d+) результатов$")
    public void addProductsToBasket(String param, int quantity) {
        if (param.equals("четные")) {
            searchResultPage.addProductsToBasket(quantity, true, false);
        } else if (param.equals("нечетные")) {
            searchResultPage.addProductsToBasket(quantity, false, true);
        } else {
            searchResultPage.addProductsToBasket(quantity, true, true);
        }
    }

    @И("^Добавить в корзину (четные|нечетные|все) результаты$")
    public void addProductsToBasket(String param) {
        addProductsToBasket(param, -1);
    }

    @И("^Перейти в корзину$")
    public void clickBasketIconCount() {
        searchResultPage.clickBasketIconCount();
    }
}
