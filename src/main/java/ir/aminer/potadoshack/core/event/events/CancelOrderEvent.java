package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.CancelOrderPacket;
import ir.aminer.potadoshack.core.network.packets.Packet;

public class CancelOrderEvent extends AuthenticatedEvent {

    public CancelOrderEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public CancelOrderPacket getData() {
        return super.getData();
    }
}
