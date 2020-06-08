package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.ErrorPacket;
import ir.aminer.potadoshack.core.network.packets.Packet;

public class ErrorEvent extends Event{
    Error error;

    public ErrorEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
        error = ((ErrorPacket)packet).getError();
    }

    public Error getError() {
        return error;
    }
}
