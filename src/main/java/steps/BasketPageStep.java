package steps;

import entities.Product;
import io.cucumber.java.ru.И;
import managers.PagesManager;
import pages.BasketPage;

/**
 * @author Sergey Nesterov
 */
public class BasketPageStep {
    private BasketPage basketPage = PagesManager.getInstance().getBasketPage();

    @И("^Закрыть всплывающее окно$")
    public void clickBasketIconCount() {
        basketPage.closeAdvertising();
    }

    @И("^Проверить присутствует ли на странице корзины надпись 'Ваша корзина N товаров'$")
    public void checkTotalContains() {
        basketPage.checkTotalContains("Ваша корзина", Product.getProducts().size() + " товар");
    }

    @И("^Проверить присутствуют ли на странице корзины все добавленные товары$")
    public void checkProductsInBasket() {
        basketPage.checkProductsInBasket(Product.getProducts());
    }

    @И("^Удалить все товары из корзины$")
    public void deleteAllProducts() {
        basketPage.deleteAllProducts();
    }

    @И("^Проверить что появилась надпись 'Корзина пуста'$")
    public void isBasketEmpty() {
        basketPage.isBasketEmpty();
    }
}
