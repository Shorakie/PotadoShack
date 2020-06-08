package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.ViewOrdersPacket;

public class ViewOrdersEvent extends AuthenticatedEvent {

    public ViewOrdersEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public ViewOrdersPacket getData() {
        return super.getData();
    }
}
