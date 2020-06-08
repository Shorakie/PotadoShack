package ir.aminer.potadoshack.core.network.packets;

public class CancelOrderPacket extends AuthenticatedPacket{
    private final int orderCode;

    public CancelOrderPacket(String jwt, int orderCode) {
        super(jwt);
        this.orderCode = orderCode;
    }

    public int getOrderCode() {
        return orderCode;
    }

    @Override
    public int getId() {
        return 6;
    }
}
