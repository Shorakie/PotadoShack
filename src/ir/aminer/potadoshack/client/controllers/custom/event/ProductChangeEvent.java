package ir.aminer.potadoshack.client.controllers.custom.event;

import ir.aminer.potadoshack.core.product.Product;

public class ProductChangeEvent {
    private final Product product;
    private final int amount;

    public ProductChangeEvent(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

}
