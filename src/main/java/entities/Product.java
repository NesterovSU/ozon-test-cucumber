package entities;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import managers.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Nesterov
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Product {

    private static List<Product> products = new ArrayList<>();

    public static void addProduct(String name, int cost) {
        products.add(new Product(name, cost));
    }

    public static List<Product> getProducts() {
        return products;
    }

    public static void clearList() {
        products.clear();
    }


    public static void attachInfo() {
        StringBuilder str = new StringBuilder();
        str.append("Списик добавленных товаров:\n");
        products.forEach(p -> str.append(p.toString()));
        str.append("Товар с наибольшей ценой: \n");
        Optional<Product> optionalProduct = products.stream().max(Comparator.comparing(Product::getCost));
        str.append(optionalProduct.isPresent() ? optionalProduct.get() : "");
        Allure.getLifecycle().addAttachment("products", "text/plain", ".txt",
                str.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String toString() {
        return this.getName() + "  --  " + this.getCost() + " руб.\n";
    }

    private String name;
    private int cost;
}
