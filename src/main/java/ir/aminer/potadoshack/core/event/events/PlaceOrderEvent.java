package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.PlaceOrderPacket;

public class PlaceOrderEvent extends AuthenticatedEvent {

    public PlaceOrderEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public PlaceOrderPacket getData() {
        return super.getData();
    }
}
