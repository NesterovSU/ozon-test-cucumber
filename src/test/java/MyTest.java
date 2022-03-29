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
public class MyTest extends BaseTest {
    @Test
    @DisplayName("Тест добавления и удаления продуктов в корзине")
    void test() {
        int countInBasket = 8;
        int costMax = 150_000;
        String search = "iphone";
        PagesManager.getInstance().getHomePage()
                .search(search);
        PagesManager.getInstance().getSearchResultPage()
                .setHighRating()
                .setNFC()
                .setCostMax(costMax);

        Assertions.assertAll(
                () -> Assertions.assertTrue(PagesManager.getInstance().getSearchResultPage().getHighRating(), "Высокий рейтинг не выбран"),
                () -> Assertions.assertTrue(PagesManager.getInstance().getSearchResultPage().getNFC(), "NFC не выбран"),
                () -> Assertions.assertEquals(costMax, PagesManager.getInstance().getSearchResultPage().getCostMax(), "Неправильно установлена максимальная цена")
        );

        List<WebElement> products = PagesManager.getInstance().getSearchResultPage().getProducts();
        System.out.println(products.size());
        for (int i = 0; i < products.size(); i++) {
            if ((i + 1) % 2 != 0) continue;
            System.out.println(i+1);
            if (PagesManager.getInstance().getSearchResultPage().addToBasket(products.get(i))) countInBasket--;
            if (countInBasket==0) break;
        }
        Product.getProducts().forEach(System.out::println);
        PagesManager.getInstance().getSearchResultPage()
                .clickBasketIconCount();
    }
}
