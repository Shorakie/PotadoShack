package ir.aminer.potadoshack.core.event.events;

import ir.aminer.potadoshack.core.auth.simplejwt.UserPayload;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.AuthenticatedPacket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.server.User;

public abstract class AuthenticatedEvent extends Event{
    public AuthenticatedEvent(Packet packet, ClientSocket sender) {
        super(packet, sender);
    }

    public User getUser() throws IllegalAccessException {
        UserPayload payload = ((AuthenticatedPacket)getData()).getJwt().getPayload();
        User client = User.fromUsername(payload.getUsername());

        if (!client.getPassword().equals(payload.getPassword()))
            throw new IllegalAccessException("Token is unauthorized");

        return client;
    }
}
