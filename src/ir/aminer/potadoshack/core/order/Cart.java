package ir.aminer.potadoshack.core.order;

import ir.aminer.potadoshack.core.product.Product;

import java.io.Serializable;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private final Map<Product, Integer> products = new HashMap<>();

    public void setProduct(Product product, final int count) {
        products.put(product, count);
    }

    public synchronized void removeProduct(Product product) {
        products.remove(product);
    }

    public synchronized void addProduct(Product product, final int count) {
        products.computeIfPresent(product, (key, value) -> value + count);
        products.computeIfAbsent(product, (key) -> count);
    }

    public Set<Map.Entry<Product, Integer>> getProducts() {
        return products.entrySet();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("--------[Cart]---------\n");
        res.append("Name\t\t\t|Amount\n").append("-----------------------\n");
        for (Map.Entry<Product, Integer> product : products.entrySet())
            res.append(product.getKey().getName()).append("\t\t\t|").append(product.getValue()).append("\n");
        return res.toString();
    }
}
