package ir.aminer.potadoshack.core.network.packets;

public class PrimitivePacket <T> extends Packet{
    private final T data;

    public PrimitivePacket(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public int getId() {
        return 4;
    }
}
