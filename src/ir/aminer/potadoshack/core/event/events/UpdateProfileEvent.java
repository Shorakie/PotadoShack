package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.UpdateProfilePacket;
import ir.aminer.potadoshack.core.network.packets.Packet;

public class UpdateProfileEvent extends AuthenticatedEvent {

    public UpdateProfileEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public UpdateProfilePacket getData() {
        return super.getData();
    }
}
