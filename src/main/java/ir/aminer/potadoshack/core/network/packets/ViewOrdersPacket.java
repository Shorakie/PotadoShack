package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.order.Order;

import java.util.HashMap;

public class ViewOrdersPacket extends AuthenticatedPacket {
    private HashMap<Integer, Order> orders;

    public ViewOrdersPacket(String jwt) {
        super(jwt);
    }

    public void setOrders(HashMap<Integer, Order> orders) {
        this.orders = orders;
    }

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    @Override
    public int getId() {
        return 3;
    }
}
