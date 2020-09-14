package ir.aminer.potadoshack.core.order;

import ir.aminer.potadoshack.core.product.Product;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Order implements Serializable {
    public enum Status {
        CREATED,
        FINALIZED,
        SETTLED_UP,
        DELIVERED
    }

    private final Cart cart;
    private Address address;
    private Status status;
    private int code;

    public Order() {
        code = -1;
        cart = new Cart();
        status = Status.CREATED;
    }

    public int getCode() {
        return code;
    }

    public void close() {
        code = hashCode();

        if (address == null)
            throw new IllegalStateException("Address is null");

        if (status.compareTo(Status.FINALIZED) < 0)
            status = Status.FINALIZED;
    }

    public void settleUp() {
        if (status.compareTo(Status.SETTLED_UP) < 0)
            status = Status.SETTLED_UP;
    }

    public void deliver() {
        if (status.compareTo(Status.DELIVERED) < 0)
            status = Status.DELIVERED;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (status.equals(Status.CREATED))
            this.address = address;
    }

    public Cart getCart() {
        return cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return code == order.code &&
                Objects.equals(cart, order.cart) &&
                Objects.equals(address, order.address) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cart, address);
    }
}
