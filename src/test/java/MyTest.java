import entities.Product;
import managers.PagesManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Sergey Nesterov
 */
public class MyTest extends BaseTestFor {
    @Test
    @DisplayName("Тест добавления и удаления продуктов в корзине")
    void test() {
        int countInBasket = 8;
        int costMax = 150_000;
        boolean mustEven = true;
        String search = "iphone";
        //поиск продукта
        PagesManager.getInstance().getHomePage()
                .search(search);
        PagesManager.getInstance().getSearchResultPage()
                .setHighRating()
                .setNFC()
                .setCostMax(costMax);
        //проверка заполнения фильтра
        Assertions.assertAll(
                () -> Assertions.assertTrue(PagesManager.getInstance().getSearchResultPage().getHighRating(), "Высокий рейтинг не выбран"),
                () -> Assertions.assertTrue(PagesManager.getInstance().getSearchResultPage().getNFC(), "NFC не выбран"),
                () -> Assertions.assertEquals(costMax, PagesManager.getInstance().getSearchResultPage().getCostMax(), "Неправильно установлена максимальная цена")
        );
        //добавление товаров в корзину
        List<WebElement> products = PagesManager.getInstance().getSearchResultPage().getProducts();
        System.out.println("Найдено товаров: " + products.size());
        for (int i = 0; i < products.size(); i++) {
            if ((i + 1) % 2 != 0 && mustEven) continue;
            if ((i + 1) % 2 == 0 && !mustEven) continue;
            if (PagesManager.getInstance().getSearchResultPage().addToBasket(products.get(i))) countInBasket--;
            if (countInBasket == 0) break;
        }
        //проверка корзины
        Assertions.assertTrue(
                PagesManager.getInstance().getBasketPage()
                        .clickBasketIconCount()
                        .closeAdv()
                        .isTotalContains("Ваша корзина", Product.getProducts().size() + " товар"),
                "Отсутствует надпись 'Ваша корзина " + Product.getProducts().size() + " товар'");
        for (Product item : Product.getProducts()) {
            Assertions.assertTrue(PagesManager.getInstance().getBasketPage()
                            .isProductInBasket(item.getName(), item.getCost()),
                    "В корзине отсутствует товар " + item);
        }
        //удаление из всех товаров из корзины
        Assertions.assertTrue(PagesManager.getInstance().getBasketPage()
                .deleteAll()
                .basketIsEmpty(), "Отсутствует заголовок 'Корзина пуста'");
        //приложение списка добавленных товаров к отчёту
        Product.attachInfo();
    }
}
