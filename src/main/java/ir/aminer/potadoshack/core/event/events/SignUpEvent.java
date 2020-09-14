package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.SignUpPacket;

public class SignUpEvent extends Event {
    public SignUpEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    @Override
    public SignUpPacket getData() {
        return super.getData();
    }
}
