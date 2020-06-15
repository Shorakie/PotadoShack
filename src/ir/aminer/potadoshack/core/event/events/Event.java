package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;

public abstract class Event {
    private final Packet packet;
    private final ClientSocket sender;

    public Event(Packet packet, ClientSocket sender) {
        this.packet = packet;
        this.sender = sender;
    }

    public ClientSocket getSender() {
        return sender;
    }

    public <P extends Packet> P getData() {
        return (P) packet;
    }
}
