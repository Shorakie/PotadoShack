package ir.aminer.potadoshack.core.eventsystem;

import ir.aminer.potadoshack.core.network.packets.Packet;

import java.net.Socket;

public interface Callback {
    void callback(Packet packet, Socket client);
}
