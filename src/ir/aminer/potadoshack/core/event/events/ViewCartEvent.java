package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.AuthenticatedPacket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.ViewCartPacket;
import ir.aminer.potadoshack.server.User;

public class ViewCartEvent extends AuthenticatedEvent {

    public ViewCartEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }
}
