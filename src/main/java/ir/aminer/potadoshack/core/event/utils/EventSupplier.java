package ir.aminer.potadoshack.core.event.utils;

import ir.aminer.potadoshack.core.event.events.Event;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;

public interface EventSupplier<E extends Event> {
    E get(Packet packet, ClientSocket sender);
}
