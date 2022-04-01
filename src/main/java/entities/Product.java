package entities;

import io.qameta.allure.Attachment;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public static void clearList(){
        products.clear();
    }

    @Attachment
    public static String attachInfo(){
        StringBuilder str = new StringBuilder();
        str.append("Списик добавленных товаров:\n");
        products.forEach(p-> str.append(p.toString()));
        str.append("Товар с наибольшей ценой: \n");
        str.append(products.stream().max(Comparator.comparing(Product::getCost)).get());
        return str.toString();
    }

    @Override
    public String toString(){
        return this.getName() + "  --  " + this.getCost() + " руб.\n";
    }

    String name;
    int cost;
}
