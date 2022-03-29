package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Nesterov
 */
@Getter
@AllArgsConstructor
@ToString
public class Product {

    private static List<Product> products = new ArrayList<>();

    public static void addProduct(String name, int cost) {
        products.add(new Product(name, cost));
    }

    public static List<Product> getProducts() {
        return products;
    }

    String name;
    int cost;
}
