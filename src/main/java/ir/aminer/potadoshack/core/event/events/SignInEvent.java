package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.SignInPacket;

public class SignInEvent extends Event {

    public SignInEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public SignInPacket getData() {
        return super.getData();
    }
}
