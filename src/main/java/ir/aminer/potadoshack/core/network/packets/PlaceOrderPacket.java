package ir.aminer.potadoshack.core.network.packets;

import ir.aminer.potadoshack.core.order.Order;

public class PlaceOrderPacket extends AuthenticatedPacket {
    private final Order order;

    public PlaceOrderPacket(String jwt, Order order) {
        super(jwt);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public int getId() {
        return 5;
    }
}
