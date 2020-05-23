package ir.aminer.potadoshack.core.network.packets;

public class ViewCartPacket extends AuthenticatedPacket{
    public ViewCartPacket(String jwt) {
        super(jwt);
    }

    @Override
    public int getId() {
        return 3;
    }
}
