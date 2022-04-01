import entities.Product;
import managers.PagesManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Nesterov
 */
public class MyTest extends BaseTestFor {
    @Test
    @DisplayName("Тест добавления восьми iphone-ов в корзину")
    void test1() {
        boolean even = true;
        boolean odd = false;
        int quantity = 8;
        int costMax = 150_000;
        String search = "iphone";
        //поиск продукта
        PagesManager.getInstance()
                .getHomePage()
                .search(search)
                .setCostMax(costMax)
                .setHighRating()
                .setNFC()
                .addProductsToBasket(quantity, even, odd)
                .clickBasketIconCount()
                .closeAdvertising()
                .checkTotalContains("Ваша корзина", Product.getProducts().size() + " товар")
                .checkProductsInBasket(Product.getProducts())
                .deleteAllProducts()
                .isBasketEmpty();
        //приложение списка добавленных товаров к отчёту
        Product.attachInfo();
    }

    @Test
    @DisplayName("Тест добавления беспроводных наушников в корзину")
    void test2() {
        boolean even = false;
        boolean odd = true;
        int costMax = 50_000;
        String search = "беспроводные наушники";
        List<String> brands = new ArrayList<>(Arrays.asList("Beats", "Samsung", "Xiaomi"));
        //поиск продукта
        PagesManager.getInstance()
                .getHomePage()
                .search(search)
                .setCostMax(costMax)
                .setBrandList(brands)
                .setHighRating()
                .addAllProductsToBasket(false, true)
                .clickBasketIconCount()
                .closeAdvertising()
                .checkTotalContains("Ваша корзина", Product.getProducts().size() + " товар")
                .checkProductsInBasket(Product.getProducts())
                .deleteAllProducts()
                .isBasketEmpty();
        //приложение списка добавленных товаров к отчёту
        Product.attachInfo();
    }

}
