package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.UpdatePasswordPacket;

public class UpdatePasswordEvent extends AuthenticatedEvent {

    public UpdatePasswordEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public UpdatePasswordPacket getData() {
        return super.getData();
    }
}
