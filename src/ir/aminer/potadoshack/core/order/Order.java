package ir.aminer.potadoshack.core.order;

import ir.aminer.potadoshack.core.product.Product;

import java.util.Map;

public class Order {
    public int getCode() {
        return code;
    }

    public enum Status {
        CREATED,
        FINALIZED,
        SETTLED_UP,
        DELIVERED;
    }

    private Cart cart;
    private String address;
    private Status status;
    private final int code;

    public Order() {
        code = 0;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (Map.Entry<Product, Integer> product : cart.getProducts())
            totalPrice += product.getKey().getPrice() * product.getValue();
        return totalPrice;
    }

    public Status getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }
}
